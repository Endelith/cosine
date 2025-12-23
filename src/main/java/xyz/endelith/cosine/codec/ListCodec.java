package xyz.endelith.cosine.codec;

import java.util.ArrayList;
import java.util.List;

import xyz.endelith.cosine.transcoder.Transcoder;

public record ListCodec<T>(Codec<T> inner) implements Codec<List<T>> {

    @Override
    public <D> D encode(Transcoder<D> transcoder, List<T> value) {
        var encodedList = transcoder.encodeList(value.size());
        for (T item : value) {
            encodedList.add(inner.encode(transcoder, item));
        }
        return encodedList.build();
    }

    @Override
    public <D> List<T> decode(Transcoder<D> transcoder, D value) {
        List<D> listResult = transcoder.decodeList(value);
        List<T> decodedList = new ArrayList<>(listResult.size());

        for (D item : listResult) {
            decodedList.add(inner.decode(transcoder, item));
        }
        return decodedList;
    }
}
