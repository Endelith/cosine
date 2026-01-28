package xyz.endelith.cosine.codec;

import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;
import xyz.endelith.cosine.transcoder.Transcoder;

public record TransformativeCodec<T, S>(
    Codec<T> inner,
    Function<T, S> to,
    Function<S, T> from
) implements Codec<S> {

    public TransformativeCodec {
        Objects.requireNonNull(inner, "inner");
        Objects.requireNonNull(to, "to");
        Objects.requireNonNull(from, "from");
    }

    @Override
    public <D> S decode(Transcoder<D> transcoder, D value) {
        T decoded = this.inner.decode(transcoder, value);
        return this.to.apply(decoded);
    }

    @Override
    public <D> D encode(Transcoder<D> transcoder, @Nullable S value) {
        if (value == null) {
            return this.inner.encode(transcoder, null);
        }
        return this.inner.encode(transcoder, this.from.apply(value));
    }
}
