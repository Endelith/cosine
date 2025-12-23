package xyz.endelith.cosine.types;

public sealed interface Either<L, R> permits Either.Left, Either.Right {

    boolean isLeft();
    boolean isRight();

    L getLeft();
    R getRight();

    record Left<L, R>(L value) implements Either<L, R> {
        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public L getLeft() {
            return value;
        }

        @Override
        public R getRight() {
            throw new UnsupportedOperationException("No right value present");
        }
    }

    record Right<L, R>(R value) implements Either<L, R> {
        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public L getLeft() {
            throw new UnsupportedOperationException("No left value present");
        }

        @Override
        public R getRight() {
            return value;
        }
    }
    
    static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }
}
