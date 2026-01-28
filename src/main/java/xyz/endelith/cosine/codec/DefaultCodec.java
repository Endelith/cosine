package xyz.endelith.cosine.codec;

import java.util.Objects;
import org.jspecify.annotations.Nullable;
import xyz.endelith.cosine.transcoder.Transcoder;

public record DefaultCodec<T>(Codec<T> inner, T def) implements Codec<T> {

    public DefaultCodec {
        Objects.requireNonNull(inner, "inner");
        Objects.requireNonNull(def, "def");
    }

    @Override
    public <D> T decode(Transcoder<D> transcoder, D value) {
        if (value == null) {
            return def;
        }
        return this.inner.decode(transcoder, value);
    }

    @Override
    public <D> D encode(Transcoder<D> transcoder, @Nullable T value) {
        if (value == null || Objects.equals(value, def)) {
            return transcoder.encodeNull();
        }
        return this.inner.encode(transcoder, value);
    }
}
