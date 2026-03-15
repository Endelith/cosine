package xyz.endelith.cosine.codec;

import java.util.Objects;
import org.jspecify.annotations.Nullable;
import xyz.endelith.cosine.transcoder.Transcoder;

public record RawValueCodec<T>() implements Codec<RawValue> {

    public record Value<D>(Transcoder<D> transcoder, D value) implements RawValue {

        public Value {
            Objects.requireNonNull(transcoder, "transcoder");
            Objects.requireNonNull(value, "value");
        }

        @Override
        @SuppressWarnings("unchecked")
        public <X> X convertTo(Transcoder<X> target) {
            if (this.transcoder == target) {
                return (X) value;
            }
            return this.transcoder.convertTo(target, this.value);
        }
    }

    @Override
    public <D> RawValue decode(Transcoder<D> transcoder, D value) {
        return RawValue.of(transcoder, value);
    }

    @Override
    public <D> D encode(Transcoder<D> transcoder, @Nullable RawValue value) {
        if (value == null) {
            throw new NullPointerException("RawValue is null");
        }

        return value.convertTo(transcoder);
    }
}
