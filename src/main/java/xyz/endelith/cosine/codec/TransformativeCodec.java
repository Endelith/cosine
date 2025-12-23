package xyz.endelith.cosine.codec;

import java.util.function.Function;

import xyz.endelith.cosine.transcoder.Transcoder;

public record TransformativeCodec<T, S>(Codec<T> inner, Function<T, S> to, Function<S, T> from) implements Codec<S> {

    @Override
    public <D> D encode(Transcoder<D> transcoder, S value) {
        return inner.encode(
                transcoder,
                from.apply(value)
        );
    }

    @Override
    public <D> S decode(Transcoder<D> transcoder, D value) {
        T innerValue = inner.decode(transcoder, value);
        return to.apply(innerValue);
    }
}
