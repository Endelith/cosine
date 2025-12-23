package xyz.endelith.cosine.codec;

import java.util.HashMap;
import java.util.Map;

import xyz.endelith.cosine.transcoder.Transcoder;

public record MapCodec<K, V>(Codec<K> keyCodec, Codec<V> valueCodec) implements Codec<Map<K, V>> {

    @Override
    public <D> D encode(Transcoder<D> transcoder, Map<K, V> value) {
        var mapBuilder = transcoder.encodeMap();

        for (Map.Entry<K, V> entry : value.entrySet()) {
            D keyResult = keyCodec.encode(transcoder, entry.getKey());
            D valueResult = valueCodec.encode(transcoder, entry.getValue());
            mapBuilder.put(keyResult, valueResult);
        }

        return mapBuilder.build();
    }

    @Override
    public <D> Map<K, V> decode(Transcoder<D> transcoder, D value) {
        var mapResult = transcoder.decodeMap(value);
        Map<K, V> decodedMap = new HashMap<>(mapResult.getSize());

        for (String key : mapResult.getKeys()) {
            K decodedKey = keyCodec.decode(
                    transcoder,
                    transcoder.encodeString(key)
            );

            V decodedValue = valueCodec.decode(
                    transcoder,
                    mapResult.getValue(key)
            );

            decodedMap.put(decodedKey, decodedValue);
        }

        return decodedMap;
    }
}
