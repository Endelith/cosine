package xyz.endelith.cosine.stream;

import io.netty.buffer.ByteBuf;
import xyz.endelith.cosine.types.Either;

public record EitherStreamCodec<L, R>(StreamCodec<L> leftCodec, StreamCodec<R> rightCodec) implements StreamCodec<Either<L, R>> {

    @Override
    public void write(ByteBuf buffer, Either<L, R> value) {
        if (value.isLeft()) {
            StreamCodec.BOOLEAN.write(buffer, true);
            leftCodec.write(buffer, value.getLeft());
        } else {
            StreamCodec.BOOLEAN.write(buffer, false);
            rightCodec.write(buffer, value.getRight());
        }
    }

    @Override
    public Either<L, R> read(ByteBuf buffer) {
        boolean isLeft = StreamCodec.BOOLEAN.read(buffer);
        if (isLeft) {
            L leftValue = leftCodec.read(buffer);
            return Either.left(leftValue);
        } else {
            R rightValue = rightCodec.read(buffer);
            return Either.right(rightValue);
        }
    }
}
