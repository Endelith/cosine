package xyz.endelith.cosine.stream;

import java.util.Objects;
import java.util.Set;
import io.netty.buffer.ByteBuf;

public record SetStreamCodec<T>(
    StreamCodec<T> parent, 
    int maxSize
) implements StreamCodec<Set<T>> {

    public SetStreamCodec {
        Objects.requireNonNull(parent, "parent");
    }

    @Override
    public void write(ByteBuf buffer, Set<T> values) {
        if (values == null) {
            BYTE.write(buffer, (byte) 0);
            return;
        }
        
        VAR_INT.write(buffer, values.size());
        for (T value : values) this.parent.write(buffer, value);
    }

    @Override
    public Set<T> read(ByteBuf buffer) {
        int size = StreamCodec.VAR_INT.read(buffer);
        
        if (size > this.maxSize) {
            throw new IllegalStateException(String.format(
                "Set size %s exceeds max size %s",
                size,
                this.maxSize
            ));
        }

        T[] values = (T[]) new Object[size];
        for (int i = 0; i < size; i++) values[i] = this.parent.read(buffer);        
        return Set.of(values);
    }
}
