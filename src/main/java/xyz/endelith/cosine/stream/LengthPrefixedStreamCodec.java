package xyz.endelith.cosine.stream;

import java.util.Objects;
import io.netty.buffer.ByteBuf;

public record LengthPrefixedStreamCodec<T>(
        StreamCodec<T> parent,
        int maxLength
) implements StreamCodec<T> {

    public LengthPrefixedStreamCodec {
        Objects.requireNonNull(parent, "parent");
    }

    @Override
    public void write(ByteBuf buffer, T value) {
        ByteBuf temp = buffer.alloc().buffer();
        try {
            this.parent.write(temp, value);
            int length = temp.readableBytes();
            if (length > this.maxLength) {
                throw new IllegalStateException(String.format(
                        "Encoded length %d exceeds maxLength %d", length, this.maxLength
                ));
            }
            VAR_INT.write(buffer, length);
            buffer.writeBytes(temp);
        } finally {
            temp.release();
        }
    }

    @Override
    public T read(ByteBuf buffer) {
        int length = VAR_INT.read(buffer);
        if (length > this.maxLength) {
            throw new IllegalStateException(String.format(
                "Encoded length %d exceeds maxLength %d", 
                length, 
                this.maxLength
            ));
        }
        if (buffer.readableBytes() < length) {
            throw new IllegalStateException(String.format(
                "Not enough bytes to read: need %d, but only %d",
                length, 
                buffer.readableBytes()
            ));
        }
        ByteBuf slice = buffer.readSlice(length);
        return this.parent.read(slice);
    }
}
