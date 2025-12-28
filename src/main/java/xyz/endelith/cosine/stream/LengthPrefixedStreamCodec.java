package xyz.endelith.cosine.stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public record LengthPrefixedStreamCodec<T>(StreamCodec<T> inner) implements StreamCodec<T> {

    @Override
    public void write(ByteBuf buffer, T value) {
        ByteBuf innerBuffer = Unpooled.buffer();
        inner.write(innerBuffer, value);
        StreamCodec.LENGTH_PREFIXED_BYTES.write(buffer, innerBuffer);
    }

    @Override
    public T read(ByteBuf buffer) {
        ByteBuf innerBuffer = StreamCodec.LENGTH_PREFIXED_BYTES.read(buffer);
        return inner.read(innerBuffer);
    }
}
