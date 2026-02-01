package xyz.endelith.cosine.codec;

import xyz.endelith.cosine.transcoder.Transcoder;

public sealed interface RawValue permits RawValueCodec.Value {

    static <D> RawValue of(Transcoder<D> transcoder, D value) {
        return new RawValueCodec.Value<>(transcoder, value);
    }

    <D> D convertTo(Transcoder<D> transcoder);
}
