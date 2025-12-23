package xyz.endelith.cosine.stream;

import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import java.util.Map;

public record MapStreamCodec<K, V>(StreamCodec<K> keyCodec, StreamCodec<V> valueCodec) implements StreamCodec<Map<K, V>> {

    @Override
    public void write(ByteBuf buffer, Map<K, V> value) {
        StreamCodec.VAR_INT.write(buffer, value.size());
        for (Map.Entry<K, V> entry : value.entrySet()) {
            keyCodec.write(buffer, entry.getKey());
            valueCodec.write(buffer, entry.getValue());
        }
    }

    @Override
    public Map<K, V> read(ByteBuf buffer) {
        int size = StreamCodec.VAR_INT.read(buffer);
        Map<K, V> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            K key = keyCodec.read(buffer);
            V val = valueCodec.read(buffer);
            map.put(key, val);
        }
        return map;
    }
}
