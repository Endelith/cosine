package xyz.endelith.cosine.codec;

import java.util.Objects;
import java.util.function.Function;

import xyz.endelith.cosine.transcoder.Transcoder;

public final class RecursiveCodec<T> implements Codec<T> {

    private final Function<Codec<T>, Codec<T>> self;
    private Codec<T> delegate;

    public RecursiveCodec(Function<Codec<T>, Codec<T>> self) {
        this.self = Objects.requireNonNull(self);
    }

    public Codec<T> delegate() {
        return delegate;
    }

    public Function<Codec<T>, Codec<T>> self() {
        return self;
    }

    private Codec<T> resolve() {
        if (delegate == null) {
            delegate = Objects.requireNonNull(self.apply(this));
        }
        return delegate;
    }

    @Override
    public <D> D encode(Transcoder<D> transcoder, T value) {
        return resolve().encode(transcoder, value);
    }

    @Override
    public <D> T decode(Transcoder<D> transcoder, D value) {
        return resolve().decode(transcoder, value);
    }
}
