package xyz.endelith.cosine.codec;

import xyz.endelith.cosine.transcoder.Transcoder;

public record OptionalCodec<T>(Codec<T> inner) implements Codec<T> {

    @Override
    public <D> D encode(Transcoder<D> transcoder, T value) {
        if (value == null) {
            return transcoder.encodeNull();
        }
        return inner.encode(transcoder, value);
    }

    @Override
    public <D> T decode(Transcoder<D> transcoder, D value) {
        try {
            return inner.decode(transcoder, value);
        } catch (Exception ignored) {
            return null;
        }
    }
}
