package xyz.endelith.cosine.stream;

import java.util.Objects;
import java.util.function.Function;

import io.netty.buffer.ByteBuf;

public record TransforStreamCodec<T, S>(
    StreamCodec<T> parent, 
    Function<T, S> to,
    Function<S, T> from
) implements StreamCodec<S> {

    public TransforStreamCodec {
        Objects.requireNonNull(parent, "parent");
        Objects.requireNonNull(to, "to");
        Objects.requireNonNull(from, "from");
    }

    @Override
    public void write(ByteBuf buffer, S value) {
        this.parent.write(buffer, this.from.apply(value));
    }

    @Override
    public S read(ByteBuf buffer) {
        return this.to.apply(this.parent.read(buffer));
    }
}
