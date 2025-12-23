package xyz.endelith.cosine.stream;

import java.util.function.Function;
import io.netty.buffer.ByteBuf;

public record UnionStreamCodec<T, K, TR extends T>(
        StreamCodec<K> keyCodec,
        Function<T, K> keyFunc,
        Function<K, StreamCodec<? extends TR>> serializers
) implements StreamCodec<T> {

    @SuppressWarnings("unchecked")
    @Override
    public void write(ByteBuf buffer, T value) {
        K key = keyFunc.apply(value);
        keyCodec.write(buffer, key);
        StreamCodec<TR> serializer = (StreamCodec<TR>) serializers.apply(key);
        serializer.write(buffer, (TR) value);
    }

    @Override
    public T read(ByteBuf buffer) {
        K key = keyCodec.read(buffer);
        StreamCodec<? extends TR> serializer = serializers.apply(key);
        return serializer.read(buffer);
    } 
}
