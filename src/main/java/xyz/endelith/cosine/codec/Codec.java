package xyz.endelith.cosine.codec;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import xyz.endelith.cosine.transcoder.Transcoder;
import xyz.endelith.cosine.type.Either;
import xyz.endelith.cosine.type.Unit;

public interface Codec<T> extends Decoder<T>, Encoder<T> {
    
    Codec<Unit> UNIT = new PrimitiveCodec<Unit>(
        new Decoder<>() {
            @Override
            public <D> Unit decode(Transcoder<D> transcoder, D value) {
                return Unit.INSTANCE;
            }
        },
        new Encoder<>() {
            @Override
            public <D> D encode(Transcoder<D> transcoder, Unit value) {
                return transcoder.encodeNull();
            }
        }
    );

    Codec<Boolean> BOOLEAN =
        new PrimitiveCodec<>(Transcoder::decodeBoolean, Transcoder::encodeBoolean);

    Codec<Byte> BYTE =
        new PrimitiveCodec<>(Transcoder::decodeByte, Transcoder::encodeByte);

    Codec<Short> SHORT =
        new PrimitiveCodec<>(Transcoder::decodeShort, Transcoder::encodeShort);

    Codec<Integer> INT =
        new PrimitiveCodec<>(Transcoder::decodeInt, Transcoder::encodeInt);

    Codec<Long> LONG =
        new PrimitiveCodec<>(Transcoder::decodeLong, Transcoder::encodeLong);

    Codec<Float> FLOAT =
        new PrimitiveCodec<>(Transcoder::decodeFloat, Transcoder::encodeFloat);

    Codec<Double> DOUBLE =
        new PrimitiveCodec<>(Transcoder::decodeDouble, Transcoder::encodeDouble);

    Codec<String> STRING =
        new PrimitiveCodec<>(Transcoder::decodeString, Transcoder::encodeString);

    Codec<byte[]> BYTE_ARRAY =
        new PrimitiveCodec<>(Transcoder::decodeByteArray, Transcoder::encodeByteArray);

    Codec<int[]> INT_ARRAY =
        new PrimitiveCodec<>(Transcoder::decodeIntArray, Transcoder::encodeIntArray);

    Codec<long[]> LONG_ARRAY =
        new PrimitiveCodec<>(Transcoder::decodeLongArray, Transcoder::encodeLongArray);
 
    Codec<UUID> UUID = 
        Codec.INT_ARRAY.transform(CodecUtils::intArrayToUuid, CodecUtils::uuidToIntArray);

    Codec<UUID> UUID_STRING = 
        STRING.transform(java.util.UUID::fromString, java.util.UUID::toString);

    Codec<UUID> UUID_COERCED = UUID.orElse(UUID_STRING);

    static <E extends Enum<E>> Codec<E> enumOf(Class<E> enumClass) {
        Objects.requireNonNull(enumClass, "enum class");
        return STRING.transform(
                value -> Enum.valueOf(enumClass, value.toUpperCase(Locale.ROOT)),
                value -> value.name().toLowerCase(Locale.ROOT));
    }

    static <T> Codec<T> recursive(Function<Codec<T>, Codec<T>> func) {
        return new RecursiveCodec<>(func).delegate();
    }

    static <T> Codec<T> forwardRef(Supplier<Codec<T>> supplier) {
        return new ForwardRefCodec<>(supplier);
    }

    static <L, R> Codec<Either<L, R>> either(Codec<L> leftCodec, Codec<R> rightCodec) {
        return new EitherCodec<>(leftCodec, rightCodec);
    }

    default <S> Codec<S> transform(Function<T, S> to, Function<S, T> from) {
        return new TransformativeCodec<>(this, to, from);
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

    default Codec<List<T>> list(int maxSize) {
        return new ListCodec<>(this, maxSize);
    }

    default Codec<List<T>> list() {
        return list(Integer.MAX_VALUE); 
    }

    default <V> Codec<Map<T, V>> mapValue(Codec<V> valueCodec, int maxSize) {
        return new MapCodec<>(this, valueCodec, maxSize);
    }

    default <V> Codec<Map<T, V>> mapValue(Codec<V> valueCodec) {
        return mapValue(valueCodec, Integer.MAX_VALUE);
    }

    default Codec<Set<T>> set(int maxSize) {
        return new SetCodec<>(this, maxSize);
    }

    default Codec<Set<T>> set() {
        return set(Integer.MAX_VALUE);
    }

    default Codec<T> optional() {
        return new OptionalCodec<>(this);
    }

    default Codec<T> defaultValue(T def) {
        return new DefaultCodec<>(this, def);
    }

    default Codec<T> orElse(Codec<T> other) {
        return new OrElseCodec<>(this, other);
    }

    default <S> Codec<T> orElse(Codec<S> other, Function<S, T> mapper) {
        return new OrElseCodec<>(this, other.transform(mapper, _ -> {
            throw new UnsupportedOperationException("unreachable");
        }));
    } 
}
