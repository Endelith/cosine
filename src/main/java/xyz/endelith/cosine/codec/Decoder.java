package xyz.endelith.cosine.codec;

import xyz.endelith.cosine.transcoder.Transcoder;

public interface Decoder<T> {
    <D> T decode(Transcoder<D> transcoder, D value);
}
