package xyz.endelith.cosine.stream;

import java.util.Objects;
import java.util.function.Function;

import io.netty.buffer.ByteBuf;

public record UnionStreamCodec<T, K, TR extends T>(
        StreamCodec<K> keyCodec,
        Function<T, K> keyFunc,
        Function<K, StreamCodec<? extends TR>> serializers
) implements StreamCodec<T> {

    public UnionStreamCodec {
        Objects.requireNonNull(keyCodec, "key codec");
        Objects.requireNonNull(keyFunc, "key func");
        Objects.requireNonNull(serializers, "serializers");
    }
 
    @Override
    @SuppressWarnings("unchecked")
    public void write(ByteBuf buffer, T value) {
        K key = this.keyFunc.apply(value);
        this.keyCodec.write(buffer, key);
        StreamCodec<TR> serializer = (StreamCodec<TR>) this.serializers.apply(key);
        serializer.write(buffer, (TR) value);
    }

    @Override
    public T read(ByteBuf buffer) {
        K key = this.keyCodec.read(buffer);
        StreamCodec<? extends TR> serializer = this.serializers.apply(key);
        return serializer.read(buffer);
    }
}
