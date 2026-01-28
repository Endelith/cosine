package xyz.endelith.cosine.codec;

import org.jspecify.annotations.Nullable;

import xyz.endelith.cosine.transcoder.Transcoder;

public interface Encoder<T> {
    <D> D encode(Transcoder<D> transcoder, @Nullable T value);
}
