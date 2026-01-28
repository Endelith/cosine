package xyz.endelith.cosine.codec;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;
import xyz.endelith.cosine.transcoder.Transcoder;

public record MapCodec<K, V>(
    Codec<K> keyCodec,
    Codec<V> valueCodec,
    int maxSize
) implements Codec<Map<K, V>> {

    public MapCodec {
        Objects.requireNonNull(keyCodec, "keyCodec");
        Objects.requireNonNull(valueCodec, "valueCodec");
    }

    @Override
    public <D> Map<K, V> decode(Transcoder<D> transcoder, D value) {
        Transcoder.VirtualMap<D> map = transcoder.decodeMap(value);

        if (map.getSize() > this.maxSize) {
            throw new IllegalStateException(String.format(
                "Map size %s exceeds max size %s",
                map.getSize(),
                this.maxSize
            ));
        }

        Map<K, V> result = new HashMap<>(map.getSize());

        for (String rawKey : map.getKeys()) {
            K key = this.keyCodec.decode(
                transcoder,
                transcoder.encodeString(rawKey)
            );

            V decodedValue = this.valueCodec.decode(
                transcoder,
                map.getValue(rawKey)
            );

            result.put(key, decodedValue);
        }

        return Map.copyOf(result);
    }

    @Override
    public <D> D encode(Transcoder<D> transcoder, @Nullable Map<K, V> value) {
        if (value == null) {
            throw new IllegalStateException("Cannot encode null Map");
        }

        if (value.size() > this.maxSize) {
            throw new IllegalStateException(String.format(
                "Map size %s exceeds max size %s",
                value.size(),
                this.maxSize
            ));
        }

        Transcoder.VirtualMapBuilder<D> builder = transcoder.encodeMap();

        for (Map.Entry<K, V> entry : value.entrySet()) {
            D encodedKey = this.keyCodec.encode(transcoder, entry.getKey());
            String key = transcoder.decodeString(encodedKey);

            D encodedValue = this.valueCodec.encode(
                transcoder,
                entry.getValue()
            );

            builder.put(key, encodedValue);
        }

        return builder.build();
    }
}
