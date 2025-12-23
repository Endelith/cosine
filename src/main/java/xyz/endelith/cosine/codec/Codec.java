package xyz.endelith.cosine.codec;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import io.netty.buffer.ByteBuf;
import xyz.endelith.cosine.transcoder.Transcoder;
import xyz.endelith.cosine.types.Either;

public interface Codec<T> {

    <D> D encode(Transcoder<D> transcoder, T value);

    <D> T decode(Transcoder<D> transcoder, D value);

    default <S> TransformativeCodec<T, S> transform(
            Function<T, S> from,
            Function<S, T> to
    ) {
        return new TransformativeCodec<>(this, from, to);
    }

    default Codec<List<T>> list() {
        return new ListCodec<>(this);
    }

    default <V> Codec<Map<T, V>> mapTo(Codec<V> valueCodec) {
        return new MapCodec<>(this, valueCodec);
    }

    default Codec<T> optional() {
        return new OptionalCodec<>(this);
    }

    default Codec<T> defaultValue(T def) {
        return new DefaultCodec<>(this, def);
    }

    default <R> StructCodec<R> union(
            Function<T, StructCodec<? extends R>> serializers,
            Function<R, T> keyFunc
    ) {
        return union("type", this, serializers, keyFunc);
    }

    default <R> StructCodec<R> union(
            Codec<T> keyCodec,
            Function<T, StructCodec<? extends R>> serializers,
            Function<R, T> keyFunc
    ) {
        return union("type", keyCodec, serializers, keyFunc);
    }

    default <R> StructCodec<R> union(
            String keyField,
            Codec<T> keyCodec,
            Function<T, StructCodec<? extends R>> serializers,
            Function<R, T> keyFunc
    ) {
        return new UnionCodec<>(keyField, keyCodec, serializers, keyFunc);
    }
    
    final class EncodingException extends RuntimeException {
        public EncodingException(String message) {
            super(message);
        }
    }
    
    final class DecodingException extends RuntimeException {
        public DecodingException(String message) {
            super(message);
        }
    }

    Codec<Boolean> BOOLEAN = new PrimitiveCodec<>(
            (t, v) -> t.encodeBoolean(v),
            (t, v) -> t.decodeBoolean(v)
    );

    Codec<Byte> BYTE = new PrimitiveCodec<>(
            (t, v) -> t.encodeByte(v),
            (t, v) -> t.decodeByte(v)
    );

    Codec<Short> SHORT = new PrimitiveCodec<>(
            (t, v) -> t.encodeShort(v),
            (t, v) -> t.decodeShort(v)
    );

    Codec<Integer> INT = new PrimitiveCodec<>(
            (t, v) -> t.encodeInt(v),
            (t, v) -> t.decodeInt(v)
    );

    Codec<Long> LONG = new PrimitiveCodec<>(
            (t, v) -> t.encodeLong(v),
            (t, v) -> t.decodeLong(v)
    );

    Codec<Float> FLOAT = new PrimitiveCodec<>(
            (t, v) -> t.encodeFloat(v),
            (t, v) -> t.decodeFloat(v)
    );

    Codec<Double> DOUBLE = new PrimitiveCodec<>(
            (t, v) -> t.encodeDouble(v),
            (t, v) -> t.decodeDouble(v)
    );

    Codec<String> STRING = new PrimitiveCodec<>(
            (t, v) -> t.encodeString(v),
            (t, v) -> t.decodeString(v)
    );

    Codec<byte[]> BYTE_ARRAY = new PrimitiveCodec<>(
            (t, v) -> t.encodeByteArray(v),
            (t, v) -> t.decodeByteArray(v)
    );

    Codec<ByteBuf> BYTE_BUFFER =
            BYTE_ARRAY.transform(
                    CodecUtils::toByteBuf,
                    CodecUtils::toByteArraySafe
            );

    Codec<int[]> INT_ARRAY = new PrimitiveCodec<>(
            (t, v) -> t.encodeIntArray(v),
            (t, v) -> t.decodeIntArray(v)
    );

    Codec<long[]> LONG_ARRAY = new PrimitiveCodec<>(
            (t, v) -> t.encodeLongArray(v),
            (t, v) -> t.decodeLongArray(v)
    );

    Codec<UUID> UUID =
            INT_ARRAY.transform(
                    CodecUtils::intArrayToUuid,
                    CodecUtils::uuidToIntArray
            );

    Codec<UUID> UUID_STRING =
            STRING.transform(
                    java.util.UUID::fromString,
                    java.util.UUID::toString
            );

    static <E extends Enum<E>> Codec<E> enumOf(Class<E> clazz) {
        return STRING.transform(
                s -> Enum.valueOf(clazz, s.toUpperCase()),
                e -> ((Enum<?>) e).name().toLowerCase()
        );
    }

    static <L, R> Codec<Either<L, R>> either(
            Codec<L> leftCodec,
            Codec<R> rightCodec
    ) {
        return new EitherCodec<>(leftCodec, rightCodec);
    }

    static <T> Codec<T> recursive(Function<Codec<T>, Codec<T>> self) {
        return new RecursiveCodec<>(self).delegate();
    }

    static <T> ForwardRefCodec<T> forwardRef(Supplier<Codec<T>> supplier) {
        return new ForwardRefCodec<>(supplier);
    }

    record PrimitiveCodec<T>(Encoder<T> encoder, Decoder<T> decoder) implements Codec<T> {

        @SuppressWarnings("unchecked")
        @Override
        public <D> D encode(Transcoder<D> transcoder, T value) {
            return (D) encoder.encode((Transcoder<Object>) transcoder, value);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <D> T decode(Transcoder<D> transcoder, D value) {
            return decoder.decode((Transcoder<Object>) transcoder, value);
        }

        private interface Encoder<T> {
            Object encode(Transcoder<Object> transcoder, T value);
        }

        private interface Decoder<T> {
            T decode(Transcoder<Object> transcoder, Object value);
        }
    }
}
