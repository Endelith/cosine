package xyz.endelith.cosine.stream;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;

public record ListStreamCodec<T>(StreamCodec<T> inner) implements StreamCodec<List<T>> {

    @Override
    public void write(ByteBuf buffer, List<T> value) {
        StreamCodec.VAR_INT.write(buffer, value.size());
        for (T item : value) {
            inner.write(buffer, item);
        }
    }

    @Override
    public List<T> read(ByteBuf buffer) {
        int size = StreamCodec.VAR_INT.read(buffer);
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(inner.read(buffer));
        }
        return list;
    }
}
