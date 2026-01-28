package xyz.endelith.cosine.codec;

import java.util.Objects;
import org.jspecify.annotations.Nullable;
import xyz.endelith.cosine.transcoder.Transcoder;

public record OptionalCodec<T>(Codec<T> inner) implements Codec<T> {

    public OptionalCodec {
        Objects.requireNonNull(inner, "inner");
    }

    @Override
    public <D> T decode(Transcoder<D> transcoder, D value) {
        try {
            return this.inner.decode(transcoder, value);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public <D> D encode(Transcoder<D> transcoder, @Nullable T value) {
        if (value == null) {
            return transcoder.encodeNull();
        }
        return this.inner.encode(transcoder, value);
    }
}
