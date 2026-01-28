package xyz.endelith.cosine.codec;

import java.util.Objects;
import org.jspecify.annotations.Nullable;
import xyz.endelith.cosine.transcoder.Transcoder;

public record PrimitiveCodec<T>(Decoder<T> decoder, Encoder<T> encoder) implements Codec<T> {

    public PrimitiveCodec {
        Objects.requireNonNull(decoder, "decoder");
        Objects.requireNonNull(encoder, "encoder");
    }

    @Override
    public <D> T decode(Transcoder<D> transcoder, D value) {
        return this.decoder.decode(transcoder, value);
    }

    @Override
    public <D> D encode(Transcoder<D> transcoder, @Nullable T value) {
        if (value == null) {
            throw new IllegalStateException("Cannot encode null value");
        }
        return this.encoder.encode(transcoder, value);
    }
}
