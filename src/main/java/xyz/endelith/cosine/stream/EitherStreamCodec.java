package xyz.endelith.cosine.stream;

import java.util.Objects;

import io.netty.buffer.ByteBuf;
import xyz.endelith.cosine.type.Either;

public record EitherStreamCodec<L, R>(
    StreamCodec<L> left,
    StreamCodec<R> right
) implements StreamCodec<Either<L, R>> {

    public EitherStreamCodec {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
    }

    @Override
    public void write(ByteBuf buffer, Either<L, R> value) {
        switch (value) {
            case Either.Left(L leftValue) -> {
                BOOLEAN.write(buffer, true);
                left.write(buffer, leftValue);
            }
            case Either.Right(R rightValue) -> {
                BOOLEAN.write(buffer, false);
                right.write(buffer, rightValue);
            }
        }
    }

    @Override
    public Either<L, R> read(ByteBuf buffer) {
        if (BOOLEAN.read(buffer)) {
            return Either.left(left.read(buffer));
        }
        return Either.right(right.read(buffer));
    }
}
