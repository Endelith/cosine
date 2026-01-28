package xyz.endelith.cosine.codec;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import xyz.endelith.cosine.transcoder.Transcoder;

public record SetCodec<T>(Codec<T> inner, int maxSize) implements Codec<Set<T>> {

    public SetCodec {
        Objects.requireNonNull(inner, "inner");
    }

    @Override
    public <D> Set<T> decode(Transcoder<D> transcoder, D value) {
        List<D> list = transcoder.decodeList(value);

        if (list.size() > this.maxSize) {
            throw new IllegalStateException(String.format(
                "Set size %s exceeds max size %s",
                list.size(),
                this.maxSize
            ));
        }

        Set<T> result = new HashSet<>(list.size());
        for (D element : list) {
            result.add(this.inner.decode(transcoder, element));
        }

        return result;
    }

    @Override
    public <D> D encode(Transcoder<D> transcoder, @Nullable Set<T> value) {
        if (value == null) {
            throw new IllegalStateException("Cannot encode null Set");
        }

        if (value.size() > this.maxSize) {
            throw new IllegalStateException(String.format(
                "Set size %s exceeds max size %s",
                value.size(),
                this.maxSize
            ));
        }

        Transcoder.ListBuilder<D> builder = transcoder.encodeList(value.size());

        for (T element : value) {
            builder.add(this.inner.encode(transcoder, element));
        }

        return builder.build();
    }
}
