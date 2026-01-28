package xyz.endelith.cosine.codec;

import java.util.Objects;
import org.jspecify.annotations.Nullable;
import xyz.endelith.cosine.transcoder.Transcoder;

public record OrElseCodec<T>(Codec<T> primary, Codec<T> secondary) implements Codec<T> {

    public OrElseCodec {
        Objects.requireNonNull(primary, "primary");
        Objects.requireNonNull(secondary, "secondary");
    }

    @Override
    public <D> T decode(Transcoder<D> transcoder, D value) {
        RuntimeException primaryError;

        try {
            return this.primary.decode(transcoder, value);
        } catch (RuntimeException ex) {
            primaryError = ex;
        }

        try {
            return this.secondary.decode(transcoder, value);
        } catch (RuntimeException ex) {
            throw primaryError;
        }
    }

    @Override
    public <D> D encode(Transcoder<D> transcoder, @Nullable T value) {
        if (value == null) {
            return this.primary.encode(transcoder, null);
        }

        RuntimeException primaryError;

        try {
            return this.primary.encode(transcoder, value);
        } catch (RuntimeException ex) {
            primaryError = ex;
        }

        try {
            return this.secondary.encode(transcoder, value);
        } catch (RuntimeException ex) {
            throw primaryError;
        }
    }
}
