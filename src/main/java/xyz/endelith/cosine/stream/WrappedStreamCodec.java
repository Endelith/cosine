package xyz.endelith.cosine.stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public record WrappedStreamCodec<T>(StreamCodec<T> inner) implements StreamCodec<T> {

    @Override
    public void write(ByteBuf buffer, T value) {
        ByteBuf innerBuffer = Unpooled.buffer();
        inner.write(innerBuffer, value);
        StreamCodec.BYTE_ARRAY.write(buffer, innerBuffer);
    }

    @Override
    public T read(ByteBuf buffer) {
        ByteBuf innerBuffer = StreamCodec.BYTE_ARRAY.read(buffer);
        return inner.read(innerBuffer);
    }
}
