package xyz.endelith.cosine.codec;

import java.util.Objects;
import java.util.function.Function;

import xyz.endelith.cosine.transcoder.Transcoder;

public final class RecursiveCodec<T> implements Codec<T> {
    
    private final Codec<T> delegate;

    public RecursiveCodec(Function<Codec<T>, Codec<T>> self) {
        Objects.requireNonNull(self, "self");
        this.delegate = Objects.requireNonNull(self.apply(this), "delegate");
    }

    @Override
    public <D> T decode(Transcoder<D> transcoder, D value) {
        return this.delegate.decode(transcoder, value);
    }

    @Override
    public <D> D encode(Transcoder<D> transcoder, T value) {
        return this.delegate.encode(transcoder, value);
    }

    public Codec<T> delegate() {
        return this.delegate;
    }
}
