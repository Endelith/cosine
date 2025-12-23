package xyz.endelith.cosine.codec;

import xyz.endelith.cosine.transcoder.Transcoder;

public record DefaultCodec<T>(Codec<T> inner, T def) implements Codec<T> {

    @Override
    public <D> D encode(Transcoder<D> transcoder, T value) {
        if (value == null || value.equals(def)) {
            return transcoder.encodeNull();
        }
        return inner.encode(transcoder, value);
    }

    @Override
    public <D> T decode(Transcoder<D> transcoder, D value) {
        try {
            return inner.decode(transcoder, value);
        } catch (Exception ignored) {
            return def;
        }
    }
}
