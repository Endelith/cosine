package xyz.endelith.cosine.codec;

import java.util.Objects;
import java.util.function.Function;
import xyz.endelith.cosine.transcoder.Transcoder;

public record UnionCodec<T, R>(
    String keyField,
    Codec<T> keyCodec,
    Function<T, StructCodec<? extends R>> serializers,
    Function<R, T> keyFunc
) implements StructCodec<R> {

    public UnionCodec {
        Objects.requireNonNull(keyField, "key field");
        Objects.requireNonNull(keyCodec, "key codec");
        Objects.requireNonNull(serializers, "serializers");
        Objects.requireNonNull(keyFunc, "key func");
    }

    @Override
    public <D> R decodeFromMap(Transcoder<D> transcoder, Transcoder.VirtualMap<D> map) {
        D rawKey = map.getValue(this.keyField);
        T key = this.keyCodec.decode(transcoder, rawKey);

        StructCodec<? extends R> serializer = this.serializers.apply(key);

        return serializer.decodeFromMap(transcoder, map);
    }

    @Override
    public <D> D encodeToMap(
        Transcoder<D> transcoder,
        R value,
        Transcoder.VirtualMapBuilder<D> map
    ) {
        T key = this.keyFunc.apply(value);

        StructCodec<R> serializer = (StructCodec<R>) this.serializers.apply(key);

        map.put(
            this.keyField,
            this.keyCodec.encode(transcoder, key)
        );

        return serializer.encodeToMap(transcoder, value, map);
    }
}
