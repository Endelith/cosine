package xyz.endelith.cosine.codec;

import xyz.endelith.cosine.transcoder.Transcoder;
import xyz.endelith.cosine.types.Either;

public record EitherCodec<L, R>(Codec<L> leftCodec, Codec<R> rightCodec) implements Codec<Either<L, R>> {

    @Override
    public <D> D encode(Transcoder<D> transcoder, Either<L, R> value) {
        if (value instanceof Either.Left<L, R> left) {
            return leftCodec.encode(transcoder, left.value());
        }
        if (value instanceof Either.Right<L, R> right) {
            return rightCodec.encode(transcoder, right.value());
        }

        throw new IllegalStateException("Unknown Either type: " + value);
    }

    @Override
    public <D> Either<L, R> decode(Transcoder<D> transcoder, D value) {
        try {
            L left = leftCodec.decode(transcoder, value);
            return Either.left(left);
        } catch (Exception ignored) {
            return Either.right(
                    rightCodec.decode(transcoder, value)
            );
        }
    }
}
