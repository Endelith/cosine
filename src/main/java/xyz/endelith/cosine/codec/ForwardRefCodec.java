package xyz.endelith.cosine.codec;

import java.util.Objects;
import java.util.function.Supplier;
import xyz.endelith.cosine.transcoder.Transcoder;

public record ForwardRefCodec<T>(Codec<T> delegate) implements Codec<T> {

    public ForwardRefCodec(Supplier<Codec<T>> supplier) {
        this(Objects.requireNonNull(supplier.get()));
    }

    @Override
    public <D> D encode(Transcoder<D> transcoder, T value) {
        return delegate.encode(transcoder, value);
    }

    @Override
    public <D> T decode(Transcoder<D> transcoder, D value) {
        return delegate.decode(transcoder, value);
    }
}
