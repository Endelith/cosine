package xyz.endelith.cosine.stream;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import io.netty.buffer.ByteBuf;

public record MapStreamCodec<K, V>(
    StreamCodec<K> parent, 
    StreamCodec<V> valueType, 
    int maxSize
) implements StreamCodec<Map<K, V>> {

    public MapStreamCodec {
        Objects.requireNonNull(parent, "parent");
        Objects.requireNonNull(valueType, "value type");
    }

    @Override
    public void write(ByteBuf buffer, Map<K, V> map) {
        StreamCodec.VAR_INT.write(buffer, map.size()); 
        for (Map.Entry<K, V> entry : map.entrySet()) {
            this.parent.write(buffer, entry.getKey());
            this.valueType.write(buffer, entry.getValue());
        }
    }

    @Override
    public Map<K, V> read(ByteBuf buffer) {
        int size = StreamCodec.VAR_INT.read(buffer);
        
        if (size > this.maxSize) {
            throw new IllegalStateException(String.format(
                "Map size %s exceeds max size %s",
                size,
                this.maxSize
            ));
        }

        Map<K, V> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            K key = this.parent.read(buffer);
            V val = this.valueType.read(buffer);
            map.put(key, val);
        }
        return map;
    }
}
