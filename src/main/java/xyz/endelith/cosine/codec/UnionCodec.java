package xyz.endelith.cosine.codec;

import java.util.function.Function;
import xyz.endelith.cosine.transcoder.Transcoder;

public record UnionCodec<T, R>(
    String keyField,
    Codec<T> keyCodec,
    Function<T, StructCodec<? extends R>> serializers,
    Function<R, T> keyFunc
) implements StructCodec<R> {

    @Override
    public <D> R decodeFromMap(Transcoder<D> transcoder, Transcoder.VirtualMap<D> map) {
        D rawKey = map.getValue(this.keyField);
        T key = this.keyCodec.decode(transcoder, rawKey);

        StructCodec<? extends R> serializer = this.serializers.apply(key);

        return serializer.decodeFromMap(transcoder, map);
    }

    @SuppressWarnings("unchecked")
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
