package xyz.endelith.cosine.transcoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface Transcoder<T> {

    T encodeNull();

    T encodeBoolean(boolean value);
    boolean decodeBoolean(T value);

    T encodeByte(byte value);
    byte decodeByte(T value);

    T encodeShort(short value);
    short decodeShort(T value);

    T encodeInt(int value);
    int decodeInt(T value);

    T encodeLong(long value);
    long decodeLong(T value);

    T encodeFloat(float value);
    float decodeFloat(T value);

    T encodeDouble(double value);
    double decodeDouble(T value);

    T encodeString(String value);
    String decodeString(T value);

    ListBuilder<T> encodeList(int size);
    List<T> decodeList(T value);

    default T emptyList() {
        return encodeList(0).build();
    }

    VirtualMapBuilder<T> encodeMap();
    VirtualMap<T> decodeMap(T value);

    default T emptyMap() {
        return encodeMap().build();
    }

    default T encodeByteArray(byte[] value) {
        ListBuilder<T> list = encodeList(value.length);
        for (byte b : value) {
            list.add(encodeByte(b));
        }
        return list.build();
    }

    default byte[] decodeByteArray(T value) {
        List<Byte> bytes = new ArrayList<>();
        for (T item : decodeList(value)) {
            bytes.add(decodeByte(item));
        }

        byte[] result = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            result[i] = bytes.get(i);
        }
        return result;
    }

    default T encodeIntArray(int[] value) {
        ListBuilder<T> list = encodeList(value.length);
        for (int i : value) {
            list.add(encodeInt(i));
        }
        return list.build();
    }

    default int[] decodeIntArray(T value) {
        List<Integer> ints = new ArrayList<>();
        for (T item : decodeList(value)) {
            ints.add(decodeInt(item));
        }

        int[] result = new int[ints.size()];
        for (int i = 0; i < ints.size(); i++) {
            result[i] = ints.get(i);
        }
        return result;
    }

    default T encodeLongArray(long[] value) {
        ListBuilder<T> list = encodeList(value.length);
        for (long l : value) {
            list.add(encodeLong(l));
        }
        return list.build();
    }

    default long[] decodeLongArray(T value) {
        List<Long> longs = new ArrayList<>();
        for (T item : decodeList(value)) {
            longs.add(decodeLong(item));
        }

        long[] result = new long[longs.size()];
        for (int i = 0; i < longs.size(); i++) {
            result[i] = longs.get(i);
        }
        return result;
    }

    interface ListBuilder<T> {
        ListBuilder<T> add(T value);
        T build();
    }

    interface VirtualMap<T> {
        Collection<String> getKeys();
        boolean hasValue(String key);
        T getValue(String key);

        default int getSize() {
            return getKeys().size();
        }

        default boolean isEmpty() {
            return getKeys().isEmpty();
        }
    }

    interface VirtualMapBuilder<T> {
        VirtualMapBuilder<T> put(T key, T value);
        VirtualMapBuilder<T> put(String key, T value);
        T build();
    }
}
