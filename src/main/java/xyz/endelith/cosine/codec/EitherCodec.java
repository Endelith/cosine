package xyz.endelith.cosine.codec;

import java.util.Objects;
import org.jspecify.annotations.Nullable;
import xyz.endelith.cosine.transcoder.Transcoder;
import xyz.endelith.cosine.type.Either;

public record EitherCodec<L, R>(
    Codec<L> leftCodec,
    Codec<R> rightCodec
) implements Codec<Either<L, R>> {

    public EitherCodec {
        Objects.requireNonNull(leftCodec, "left codec");
        Objects.requireNonNull(rightCodec, "right codec");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <D> Either<L, R> decode(Transcoder<D> transcoder, D value) {
        if (value instanceof Either.Left<?, ?> left) {
            L decoded = this.leftCodec.decode(transcoder, (D) left.value());
            return Either.left(decoded);
        }

        if (value instanceof Either.Right<?, ?> right) {
            R decoded = this.rightCodec.decode(transcoder, (D) right.value());
            return Either.right(decoded);
        }

        throw new IllegalStateException("Unknown Either type: " + value);
    }

    @Override
    public <D> D encode(Transcoder<D> transcoder, @Nullable Either<L, R> value) {
        if (value == null) return null;

        return value.unify(
            left -> this.leftCodec.encode(transcoder, left),
            right -> this.rightCodec.encode(transcoder, right)
        );
    }
}
