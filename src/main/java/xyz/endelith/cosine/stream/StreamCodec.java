package xyz.endelith.cosine.stream;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import io.netty.buffer.ByteBuf;
import xyz.endelith.cosine.codec.CodecUtils;
import xyz.endelith.cosine.codec.StructCodec.Function2;
import xyz.endelith.cosine.codec.StructCodec.Function3;
import xyz.endelith.cosine.codec.StructCodec.Function4;
import xyz.endelith.cosine.codec.StructCodec.Function5;
import xyz.endelith.cosine.codec.StructCodec.Function6;
import xyz.endelith.cosine.codec.StructCodec.Function7;
import xyz.endelith.cosine.codec.StructCodec.Function8;
import xyz.endelith.cosine.codec.StructCodec.Function9;
import xyz.endelith.cosine.codec.StructCodec.Function10;
import xyz.endelith.cosine.codec.StructCodec.Function11;
import xyz.endelith.cosine.codec.StructCodec.Function12;
import xyz.endelith.cosine.codec.StructCodec.Function13;
import xyz.endelith.cosine.codec.StructCodec.Function14;
import xyz.endelith.cosine.codec.StructCodec.Function15;
import xyz.endelith.cosine.codec.StructCodec.Function16;
import xyz.endelith.cosine.codec.StructCodec.Function17;
import xyz.endelith.cosine.codec.StructCodec.Function18;
import xyz.endelith.cosine.codec.StructCodec.Function19;
import xyz.endelith.cosine.codec.StructCodec.Function20;

public interface StreamCodec<T> {

    void write(ByteBuf buffer, T value);

    T read(ByteBuf buffer);

    default OptionalStreamCodec<T> optional() {
        return new OptionalStreamCodec<>(this);
    }

    default DefaultStreamCodec<T> defaultValue(T defaultValue) {
        return new DefaultStreamCodec<>(this, defaultValue);
    }

    default <S> TransformativeStreamCodec<T, S> transform(
            Function<S, T> from,
            Function<T, S> to
    ) {
        return new TransformativeStreamCodec<>(this, to, from);
    }

    default <V> MapStreamCodec<T, V> mapTo(StreamCodec<V> valueCodec) {
        return new MapStreamCodec<>(this, valueCodec);
    }

    default ListStreamCodec<T> list() {
        return new ListStreamCodec<>(this);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    default <V, TR extends V> UnionStreamCodec<V, T, TR> union( 
        Function<T, ? extends StreamCodec<? extends TR>> serializers,
        Function<V, T> keyFunc
    ) {
        return new UnionStreamCodec(this, keyFunc, serializers);
    }

    default WrappedStreamCodec<T> wrapped() {
        return new WrappedStreamCodec<>(this);
    }

    StreamCodec<Void> UNIT = new StreamCodec<>() {
        @Override
        public void write(ByteBuf buffer, Void value) { }

        @Override
        public Void read(ByteBuf buffer) {
            return null;
        }
    };

    StreamCodec<Boolean> BOOLEAN = new StreamCodec<>() {
        @Override
        public void write(ByteBuf buffer, Boolean value) {
            buffer.writeBoolean(value);
        }

        @Override
        public Boolean read(ByteBuf buffer) {
            return buffer.readBoolean();
        }
    };

    StreamCodec<Byte> BYTE = new StreamCodec<>() {
        @Override
        public void write(ByteBuf buffer, Byte value) {
            buffer.writeByte(value);
        }

        @Override
        public Byte read(ByteBuf buffer) {
            return buffer.readByte();
        }
    };

    StreamCodec<Short> SHORT = new StreamCodec<>() {
        @Override
        public void write(ByteBuf buffer, Short value) {
            buffer.writeShort(value);
        }

        @Override
        public Short read(ByteBuf buffer) {
            return buffer.readShort();
        }
    };

    StreamCodec<Integer> UNSIGNED_SHORT = new StreamCodec<>() {
        @Override
        public void write(ByteBuf buffer, Integer value) {
            buffer.writeShort(value & 0xFFFF);
        }

        @Override
        public Integer read(ByteBuf buffer) {
            return buffer.readUnsignedShort();
        }
    };

    StreamCodec<Integer> INT = new StreamCodec<>() {
        @Override
        public void write(ByteBuf buffer, Integer value) {
            buffer.writeInt(value);
        }

        @Override
        public Integer read(ByteBuf buffer) {
            return buffer.readInt();
        }
    };

    StreamCodec<Integer> VAR_INT = new StreamCodec<>() {
        @Override
        public void write(ByteBuf buffer, Integer value) {
            CodecUtils.writeVarInt(buffer, value);
        }

        @Override
        public Integer read(ByteBuf buffer) {
            return CodecUtils.readVarInt(buffer);
        }
    };

    StreamCodec<Long> LONG = new StreamCodec<>() {
        @Override
        public void write(ByteBuf buffer, Long value) {
            buffer.writeLong(value);
        }

        @Override
        public Long read(ByteBuf buffer) {
            return buffer.readLong();
        }
    };

    StreamCodec<Float> FLOAT = new StreamCodec<>() {
        @Override
        public void write(ByteBuf buffer, Float value) {
            buffer.writeFloat(value);
        }

        @Override
        public Float read(ByteBuf buffer) {
            return buffer.readFloat();
        }
    };

    StreamCodec<Double> DOUBLE = new StreamCodec<>() {
        @Override
        public void write(ByteBuf buffer, Double value) {
            buffer.writeDouble(value);
        }

        @Override
        public Double read(ByteBuf buffer) {
            return buffer.readDouble();
        }
    };

    StreamCodec<String> STRING = new StreamCodec<>() {
        @Override
        public void write(ByteBuf buffer, String value) {
            CodecUtils.writeString(buffer, value);
        }

        @Override
        public String read(ByteBuf buffer) {
            return CodecUtils.readString(buffer);
        }
    };

    public static final StreamCodec<ByteBuf> RAW_BYTES = new StreamCodec<>() {
    
        @Override
        public void write(ByteBuf out, ByteBuf value) {
            out.writeBytes(value, value.readerIndex(), value.readableBytes());
        }
    
        @Override
        public ByteBuf read(ByteBuf in) {
            return in.readRetainedSlice(in.readableBytes());
        }
    };
    
    public static final StreamCodec<byte[]> BYTE_ARRAY = new StreamCodec<>() {
    
        @Override
        public void write(ByteBuf out, byte[] value) {
            VAR_INT.write(out, value.length);
            out.writeBytes(value);
        }
    
        @Override
        public byte[] read(ByteBuf in) {
            int length = VAR_INT.read(in);
    
            byte[] data = new byte[length];
            in.readBytes(data);
    
            return data;
        }
    };

    StreamCodec<ByteBuf> LENGTH_PREFIXED_BYTES = new StreamCodec<>() {
        @Override
        public void write(ByteBuf buffer, ByteBuf value) {
            VAR_INT.write(buffer, value.readableBytes());
            RAW_BYTES.write(buffer, value);
        }
    
        @Override
        public ByteBuf read(ByteBuf buffer) {
            int size = VAR_INT.read(buffer);
            return buffer.readBytes(size);
        }
    };

    StreamCodec<UUID> UUID = new StreamCodec<>() {
        @Override
        public void write(ByteBuf buffer, UUID value) {
            LONG.write(buffer, value.getMostSignificantBits());
            LONG.write(buffer, value.getLeastSignificantBits());
        }

        @Override
        public UUID read(ByteBuf buffer) {
            long most = LONG.read(buffer);
            long least = LONG.read(buffer);
            return new UUID(most, least);
        }
    };

    StreamCodec<long[]> LONG_ARRAY = new StreamCodec<>() {
        @Override
        public void write(ByteBuf buffer, long[] value) {
            VAR_INT.write(buffer, value.length);
            for (long l : value) buffer.writeLong(l);
        }

        @Override
        public long[] read(ByteBuf buffer) {
            int size = VAR_INT.read(buffer);
            long[] arr = new long[size];
            for (int i = 0; i < size; i++) arr[i] = buffer.readLong();
            return arr;
        }
    };

    static <T> RecursiveStreamCodec<T> recursive(Function<StreamCodec<T>, StreamCodec<T>> self) {
        return new RecursiveStreamCodec<>(self);
    }

    StreamCodec<UUID> UUID_STRING =
            STRING.transform(
                    java.util.UUID::toString,
                    java.util.UUID::fromString
            );

    static <E extends Enum<E>> StreamCodec<E> enumOf(Class<E> clazz) {
        return new EnumStreamCodec<>(clazz);
    }

    static <E extends Enum<E>> StreamCodec<E> enumString(Class<E> clazz) {
        return STRING.transform(
            e -> ((Enum<?>) e).name().toLowerCase(),
            s -> Enum.valueOf(clazz, s.toUpperCase())
        );
    }

    static FixedBitSetStreamCodec fixedBitSet(int length) {
        return new FixedBitSetStreamCodec(length);
    }

    static <T> LengthPrefixedStreamCodec<T> lengthPrefixed(StreamCodec<T> codec) {
        return new LengthPrefixedStreamCodec<>(codec);
    }

    static <L, R> EitherStreamCodec<L, R> either(StreamCodec<L> leftCodec, StreamCodec<R> rightCodec) {
        return new EitherStreamCodec<>(leftCodec, rightCodec);
    }

    public static <R> StreamCodec<R> of(Supplier<R> supplier) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) { }

            @Override
            public R read(ByteBuf buffer) {
                return supplier.get();
            }
        };
    }

    public static <R, P1> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        Function<P1, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                return supplier.apply(result1);
            }
        };
    }

    public static <R, P1, P2> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        Function2<P1, P2, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                return supplier.apply(result1, result2);
            }
        };
    }

    public static <R, P1, P2, P3> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        Function3<P1, P2, P3, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                return supplier.apply(result1, result2, result3);
            }
        };
    }

    public static <R, P1, P2, P3, P4> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        Function4<P1, P2, P3, P4, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                return supplier.apply(result1, result2, result3, result4);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        Function5<P1, P2, P3, P4, P5, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5, P6> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        StreamCodec<P6> codec6, Function<R, P6> getter6,
        Function6<P1, P2, P3, P4, P5, P6, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
                codec6.write(buffer, getter6.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                P6 result6 = codec6.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5, result6);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5, P6, P7> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        StreamCodec<P6> codec6, Function<R, P6> getter6,
        StreamCodec<P7> codec7, Function<R, P7> getter7,
        Function7<P1, P2, P3, P4, P5, P6, P7, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
                codec6.write(buffer, getter6.apply(value));
                codec7.write(buffer, getter7.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                P6 result6 = codec6.read(buffer);
                P7 result7 = codec7.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5, result6, result7);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5, P6, P7, P8> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        StreamCodec<P6> codec6, Function<R, P6> getter6,
        StreamCodec<P7> codec7, Function<R, P7> getter7,
        StreamCodec<P8> codec8, Function<R, P8> getter8,
        Function8<P1, P2, P3, P4, P5, P6, P7, P8, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
                codec6.write(buffer, getter6.apply(value));
                codec7.write(buffer, getter7.apply(value));
                codec8.write(buffer, getter8.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                P6 result6 = codec6.read(buffer);
                P7 result7 = codec7.read(buffer);
                P8 result8 = codec8.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5, result6, result7, result8);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5, P6, P7, P8, P9> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        StreamCodec<P6> codec6, Function<R, P6> getter6,
        StreamCodec<P7> codec7, Function<R, P7> getter7,
        StreamCodec<P8> codec8, Function<R, P8> getter8,
        StreamCodec<P9> codec9, Function<R, P9> getter9,
        Function9<P1, P2, P3, P4, P5, P6, P7, P8, P9, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
                codec6.write(buffer, getter6.apply(value));
                codec7.write(buffer, getter7.apply(value));
                codec8.write(buffer, getter8.apply(value));
                codec9.write(buffer, getter9.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                P6 result6 = codec6.read(buffer);
                P7 result7 = codec7.read(buffer);
                P8 result8 = codec8.read(buffer);
                P9 result9 = codec9.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5, result6, result7, result8, result9);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        StreamCodec<P6> codec6, Function<R, P6> getter6,
        StreamCodec<P7> codec7, Function<R, P7> getter7,
        StreamCodec<P8> codec8, Function<R, P8> getter8,
        StreamCodec<P9> codec9, Function<R, P9> getter9,
        StreamCodec<P10> codec10, Function<R, P10> getter10,
        Function10<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
                codec6.write(buffer, getter6.apply(value));
                codec7.write(buffer, getter7.apply(value));
                codec8.write(buffer, getter8.apply(value));
                codec9.write(buffer, getter9.apply(value));
                codec10.write(buffer, getter10.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                P6 result6 = codec6.read(buffer);
                P7 result7 = codec7.read(buffer);
                P8 result8 = codec8.read(buffer);
                P9 result9 = codec9.read(buffer);
                P10 result10 = codec10.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        StreamCodec<P6> codec6, Function<R, P6> getter6,
        StreamCodec<P7> codec7, Function<R, P7> getter7,
        StreamCodec<P8> codec8, Function<R, P8> getter8,
        StreamCodec<P9> codec9, Function<R, P9> getter9,
        StreamCodec<P10> codec10, Function<R, P10> getter10,
        StreamCodec<P11> codec11, Function<R, P11> getter11,
        Function11<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
                codec6.write(buffer, getter6.apply(value));
                codec7.write(buffer, getter7.apply(value));
                codec8.write(buffer, getter8.apply(value));
                codec9.write(buffer, getter9.apply(value));
                codec10.write(buffer, getter10.apply(value));
                codec11.write(buffer, getter11.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                P6 result6 = codec6.read(buffer);
                P7 result7 = codec7.read(buffer);
                P8 result8 = codec8.read(buffer);
                P9 result9 = codec9.read(buffer);
                P10 result10 = codec10.read(buffer);
                P11 result11 = codec11.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        StreamCodec<P6> codec6, Function<R, P6> getter6,
        StreamCodec<P7> codec7, Function<R, P7> getter7,
        StreamCodec<P8> codec8, Function<R, P8> getter8,
        StreamCodec<P9> codec9, Function<R, P9> getter9,
        StreamCodec<P10> codec10, Function<R, P10> getter10,
        StreamCodec<P11> codec11, Function<R, P11> getter11,
        StreamCodec<P12> codec12, Function<R, P12> getter12,
        Function12<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
                codec6.write(buffer, getter6.apply(value));
                codec7.write(buffer, getter7.apply(value));
                codec8.write(buffer, getter8.apply(value));
                codec9.write(buffer, getter9.apply(value));
                codec10.write(buffer, getter10.apply(value));
                codec11.write(buffer, getter11.apply(value));
                codec12.write(buffer, getter12.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                P6 result6 = codec6.read(buffer);
                P7 result7 = codec7.read(buffer);
                P8 result8 = codec8.read(buffer);
                P9 result9 = codec9.read(buffer);
                P10 result10 = codec10.read(buffer);
                P11 result11 = codec11.read(buffer);
                P12 result12 = codec12.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        StreamCodec<P6> codec6, Function<R, P6> getter6,
        StreamCodec<P7> codec7, Function<R, P7> getter7,
        StreamCodec<P8> codec8, Function<R, P8> getter8,
        StreamCodec<P9> codec9, Function<R, P9> getter9,
        StreamCodec<P10> codec10, Function<R, P10> getter10,
        StreamCodec<P11> codec11, Function<R, P11> getter11,
        StreamCodec<P12> codec12, Function<R, P12> getter12,
        StreamCodec<P13> codec13, Function<R, P13> getter13,
        Function13<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
                codec6.write(buffer, getter6.apply(value));
                codec7.write(buffer, getter7.apply(value));
                codec8.write(buffer, getter8.apply(value));
                codec9.write(buffer, getter9.apply(value));
                codec10.write(buffer, getter10.apply(value));
                codec11.write(buffer, getter11.apply(value));
                codec12.write(buffer, getter12.apply(value));
                codec13.write(buffer, getter13.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                P6 result6 = codec6.read(buffer);
                P7 result7 = codec7.read(buffer);
                P8 result8 = codec8.read(buffer);
                P9 result9 = codec9.read(buffer);
                P10 result10 = codec10.read(buffer);
                P11 result11 = codec11.read(buffer);
                P12 result12 = codec12.read(buffer);
                P13 result13 = codec13.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        StreamCodec<P6> codec6, Function<R, P6> getter6,
        StreamCodec<P7> codec7, Function<R, P7> getter7,
        StreamCodec<P8> codec8, Function<R, P8> getter8,
        StreamCodec<P9> codec9, Function<R, P9> getter9,
        StreamCodec<P10> codec10, Function<R, P10> getter10,
        StreamCodec<P11> codec11, Function<R, P11> getter11,
        StreamCodec<P12> codec12, Function<R, P12> getter12,
        StreamCodec<P13> codec13, Function<R, P13> getter13,
        StreamCodec<P14> codec14, Function<R, P14> getter14,
        Function14<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
                codec6.write(buffer, getter6.apply(value));
                codec7.write(buffer, getter7.apply(value));
                codec8.write(buffer, getter8.apply(value));
                codec9.write(buffer, getter9.apply(value));
                codec10.write(buffer, getter10.apply(value));
                codec11.write(buffer, getter11.apply(value));
                codec12.write(buffer, getter12.apply(value));
                codec13.write(buffer, getter13.apply(value));
                codec14.write(buffer, getter14.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                P6 result6 = codec6.read(buffer);
                P7 result7 = codec7.read(buffer);
                P8 result8 = codec8.read(buffer);
                P9 result9 = codec9.read(buffer);
                P10 result10 = codec10.read(buffer);
                P11 result11 = codec11.read(buffer);
                P12 result12 = codec12.read(buffer);
                P13 result13 = codec13.read(buffer);
                P14 result14 = codec14.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13, result14);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        StreamCodec<P6> codec6, Function<R, P6> getter6,
        StreamCodec<P7> codec7, Function<R, P7> getter7,
        StreamCodec<P8> codec8, Function<R, P8> getter8,
        StreamCodec<P9> codec9, Function<R, P9> getter9,
        StreamCodec<P10> codec10, Function<R, P10> getter10,
        StreamCodec<P11> codec11, Function<R, P11> getter11,
        StreamCodec<P12> codec12, Function<R, P12> getter12,
        StreamCodec<P13> codec13, Function<R, P13> getter13,
        StreamCodec<P14> codec14, Function<R, P14> getter14,
        StreamCodec<P15> codec15, Function<R, P15> getter15,
        Function15<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
                codec6.write(buffer, getter6.apply(value));
                codec7.write(buffer, getter7.apply(value));
                codec8.write(buffer, getter8.apply(value));
                codec9.write(buffer, getter9.apply(value));
                codec10.write(buffer, getter10.apply(value));
                codec11.write(buffer, getter11.apply(value));
                codec12.write(buffer, getter12.apply(value));
                codec13.write(buffer, getter13.apply(value));
                codec14.write(buffer, getter14.apply(value));
                codec15.write(buffer, getter15.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                P6 result6 = codec6.read(buffer);
                P7 result7 = codec7.read(buffer);
                P8 result8 = codec8.read(buffer);
                P9 result9 = codec9.read(buffer);
                P10 result10 = codec10.read(buffer);
                P11 result11 = codec11.read(buffer);
                P12 result12 = codec12.read(buffer);
                P13 result13 = codec13.read(buffer);
                P14 result14 = codec14.read(buffer);
                P15 result15 = codec15.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13, result14, result15);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        StreamCodec<P6> codec6, Function<R, P6> getter6,
        StreamCodec<P7> codec7, Function<R, P7> getter7,
        StreamCodec<P8> codec8, Function<R, P8> getter8,
        StreamCodec<P9> codec9, Function<R, P9> getter9,
        StreamCodec<P10> codec10, Function<R, P10> getter10,
        StreamCodec<P11> codec11, Function<R, P11> getter11,
        StreamCodec<P12> codec12, Function<R, P12> getter12,
        StreamCodec<P13> codec13, Function<R, P13> getter13,
        StreamCodec<P14> codec14, Function<R, P14> getter14,
        StreamCodec<P15> codec15, Function<R, P15> getter15,
        StreamCodec<P16> codec16, Function<R, P16> getter16,
        Function16<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
                codec6.write(buffer, getter6.apply(value));
                codec7.write(buffer, getter7.apply(value));
                codec8.write(buffer, getter8.apply(value));
                codec9.write(buffer, getter9.apply(value));
                codec10.write(buffer, getter10.apply(value));
                codec11.write(buffer, getter11.apply(value));
                codec12.write(buffer, getter12.apply(value));
                codec13.write(buffer, getter13.apply(value));
                codec14.write(buffer, getter14.apply(value));
                codec15.write(buffer, getter15.apply(value));
                codec16.write(buffer, getter16.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                P6 result6 = codec6.read(buffer);
                P7 result7 = codec7.read(buffer);
                P8 result8 = codec8.read(buffer);
                P9 result9 = codec9.read(buffer);
                P10 result10 = codec10.read(buffer);
                P11 result11 = codec11.read(buffer);
                P12 result12 = codec12.read(buffer);
                P13 result13 = codec13.read(buffer);
                P14 result14 = codec14.read(buffer);
                P15 result15 = codec15.read(buffer);
                P16 result16 = codec16.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13, result14, result15, result16);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        StreamCodec<P6> codec6, Function<R, P6> getter6,
        StreamCodec<P7> codec7, Function<R, P7> getter7,
        StreamCodec<P8> codec8, Function<R, P8> getter8,
        StreamCodec<P9> codec9, Function<R, P9> getter9,
        StreamCodec<P10> codec10, Function<R, P10> getter10,
        StreamCodec<P11> codec11, Function<R, P11> getter11,
        StreamCodec<P12> codec12, Function<R, P12> getter12,
        StreamCodec<P13> codec13, Function<R, P13> getter13,
        StreamCodec<P14> codec14, Function<R, P14> getter14,
        StreamCodec<P15> codec15, Function<R, P15> getter15,
        StreamCodec<P16> codec16, Function<R, P16> getter16,
        StreamCodec<P17> codec17, Function<R, P17> getter17,
        Function17<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
                codec6.write(buffer, getter6.apply(value));
                codec7.write(buffer, getter7.apply(value));
                codec8.write(buffer, getter8.apply(value));
                codec9.write(buffer, getter9.apply(value));
                codec10.write(buffer, getter10.apply(value));
                codec11.write(buffer, getter11.apply(value));
                codec12.write(buffer, getter12.apply(value));
                codec13.write(buffer, getter13.apply(value));
                codec14.write(buffer, getter14.apply(value));
                codec15.write(buffer, getter15.apply(value));
                codec16.write(buffer, getter16.apply(value));
                codec17.write(buffer, getter17.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                P6 result6 = codec6.read(buffer);
                P7 result7 = codec7.read(buffer);
                P8 result8 = codec8.read(buffer);
                P9 result9 = codec9.read(buffer);
                P10 result10 = codec10.read(buffer);
                P11 result11 = codec11.read(buffer);
                P12 result12 = codec12.read(buffer);
                P13 result13 = codec13.read(buffer);
                P14 result14 = codec14.read(buffer);
                P15 result15 = codec15.read(buffer);
                P16 result16 = codec16.read(buffer);
                P17 result17 = codec17.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13, result14, result15, result16, result17);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        StreamCodec<P6> codec6, Function<R, P6> getter6,
        StreamCodec<P7> codec7, Function<R, P7> getter7,
        StreamCodec<P8> codec8, Function<R, P8> getter8,
        StreamCodec<P9> codec9, Function<R, P9> getter9,
        StreamCodec<P10> codec10, Function<R, P10> getter10,
        StreamCodec<P11> codec11, Function<R, P11> getter11,
        StreamCodec<P12> codec12, Function<R, P12> getter12,
        StreamCodec<P13> codec13, Function<R, P13> getter13,
        StreamCodec<P14> codec14, Function<R, P14> getter14,
        StreamCodec<P15> codec15, Function<R, P15> getter15,
        StreamCodec<P16> codec16, Function<R, P16> getter16,
        StreamCodec<P17> codec17, Function<R, P17> getter17,
        StreamCodec<P18> codec18, Function<R, P18> getter18,
        Function18<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
                codec6.write(buffer, getter6.apply(value));
                codec7.write(buffer, getter7.apply(value));
                codec8.write(buffer, getter8.apply(value));
                codec9.write(buffer, getter9.apply(value));
                codec10.write(buffer, getter10.apply(value));
                codec11.write(buffer, getter11.apply(value));
                codec12.write(buffer, getter12.apply(value));
                codec13.write(buffer, getter13.apply(value));
                codec14.write(buffer, getter14.apply(value));
                codec15.write(buffer, getter15.apply(value));
                codec16.write(buffer, getter16.apply(value));
                codec17.write(buffer, getter17.apply(value));
                codec18.write(buffer, getter18.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                P6 result6 = codec6.read(buffer);
                P7 result7 = codec7.read(buffer);
                P8 result8 = codec8.read(buffer);
                P9 result9 = codec9.read(buffer);
                P10 result10 = codec10.read(buffer);
                P11 result11 = codec11.read(buffer);
                P12 result12 = codec12.read(buffer);
                P13 result13 = codec13.read(buffer);
                P14 result14 = codec14.read(buffer);
                P15 result15 = codec15.read(buffer);
                P16 result16 = codec16.read(buffer);
                P17 result17 = codec17.read(buffer);
                P18 result18 = codec18.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13, result14, result15, result16, result17, result18);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        StreamCodec<P6> codec6, Function<R, P6> getter6,
        StreamCodec<P7> codec7, Function<R, P7> getter7,
        StreamCodec<P8> codec8, Function<R, P8> getter8,
        StreamCodec<P9> codec9, Function<R, P9> getter9,
        StreamCodec<P10> codec10, Function<R, P10> getter10,
        StreamCodec<P11> codec11, Function<R, P11> getter11,
        StreamCodec<P12> codec12, Function<R, P12> getter12,
        StreamCodec<P13> codec13, Function<R, P13> getter13,
        StreamCodec<P14> codec14, Function<R, P14> getter14,
        StreamCodec<P15> codec15, Function<R, P15> getter15,
        StreamCodec<P16> codec16, Function<R, P16> getter16,
        StreamCodec<P17> codec17, Function<R, P17> getter17,
        StreamCodec<P18> codec18, Function<R, P18> getter18,
        StreamCodec<P19> codec19, Function<R, P19> getter19,
        Function19<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
                codec6.write(buffer, getter6.apply(value));
                codec7.write(buffer, getter7.apply(value));
                codec8.write(buffer, getter8.apply(value));
                codec9.write(buffer, getter9.apply(value));
                codec10.write(buffer, getter10.apply(value));
                codec11.write(buffer, getter11.apply(value));
                codec12.write(buffer, getter12.apply(value));
                codec13.write(buffer, getter13.apply(value));
                codec14.write(buffer, getter14.apply(value));
                codec15.write(buffer, getter15.apply(value));
                codec16.write(buffer, getter16.apply(value));
                codec17.write(buffer, getter17.apply(value));
                codec18.write(buffer, getter18.apply(value));
                codec19.write(buffer, getter19.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                P6 result6 = codec6.read(buffer);
                P7 result7 = codec7.read(buffer);
                P8 result8 = codec8.read(buffer);
                P9 result9 = codec9.read(buffer);
                P10 result10 = codec10.read(buffer);
                P11 result11 = codec11.read(buffer);
                P12 result12 = codec12.read(buffer);
                P13 result13 = codec13.read(buffer);
                P14 result14 = codec14.read(buffer);
                P15 result15 = codec15.read(buffer);
                P16 result16 = codec16.read(buffer);
                P17 result17 = codec17.read(buffer);
                P18 result18 = codec18.read(buffer);
                P19 result19 = codec19.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13, result14, result15, result16, result17, result18, result19);
            }
        };
    }

    public static <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20> StreamCodec<R> of(
        StreamCodec<P1> codec1, Function<R, P1> getter1,
        StreamCodec<P2> codec2, Function<R, P2> getter2,
        StreamCodec<P3> codec3, Function<R, P3> getter3,
        StreamCodec<P4> codec4, Function<R, P4> getter4,
        StreamCodec<P5> codec5, Function<R, P5> getter5,
        StreamCodec<P6> codec6, Function<R, P6> getter6,
        StreamCodec<P7> codec7, Function<R, P7> getter7,
        StreamCodec<P8> codec8, Function<R, P8> getter8,
        StreamCodec<P9> codec9, Function<R, P9> getter9,
        StreamCodec<P10> codec10, Function<R, P10> getter10,
        StreamCodec<P11> codec11, Function<R, P11> getter11,
        StreamCodec<P12> codec12, Function<R, P12> getter12,
        StreamCodec<P13> codec13, Function<R, P13> getter13,
        StreamCodec<P14> codec14, Function<R, P14> getter14,
        StreamCodec<P15> codec15, Function<R, P15> getter15,
        StreamCodec<P16> codec16, Function<R, P16> getter16,
        StreamCodec<P17> codec17, Function<R, P17> getter17,
        StreamCodec<P18> codec18, Function<R, P18> getter18,
        StreamCodec<P19> codec19, Function<R, P19> getter19,
        StreamCodec<P20> codec20, Function<R, P20> getter20,
        Function20<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, R> supplier
    ) {
        return new StreamCodec<>() {
            @Override
            public void write(ByteBuf buffer, R value) {
                codec1.write(buffer, getter1.apply(value));
                codec2.write(buffer, getter2.apply(value));
                codec3.write(buffer, getter3.apply(value));
                codec4.write(buffer, getter4.apply(value));
                codec5.write(buffer, getter5.apply(value));
                codec6.write(buffer, getter6.apply(value));
                codec7.write(buffer, getter7.apply(value));
                codec8.write(buffer, getter8.apply(value));
                codec9.write(buffer, getter9.apply(value));
                codec10.write(buffer, getter10.apply(value));
                codec11.write(buffer, getter11.apply(value));
                codec12.write(buffer, getter12.apply(value));
                codec13.write(buffer, getter13.apply(value));
                codec14.write(buffer, getter14.apply(value));
                codec15.write(buffer, getter15.apply(value));
                codec16.write(buffer, getter16.apply(value));
                codec17.write(buffer, getter17.apply(value));
                codec18.write(buffer, getter18.apply(value));
                codec19.write(buffer, getter19.apply(value));
                codec20.write(buffer, getter20.apply(value));
            }

            @Override
            public R read(ByteBuf buffer) {
                P1 result1 = codec1.read(buffer);
                P2 result2 = codec2.read(buffer);
                P3 result3 = codec3.read(buffer);
                P4 result4 = codec4.read(buffer);
                P5 result5 = codec5.read(buffer);
                P6 result6 = codec6.read(buffer);
                P7 result7 = codec7.read(buffer);
                P8 result8 = codec8.read(buffer);
                P9 result9 = codec9.read(buffer);
                P10 result10 = codec10.read(buffer);
                P11 result11 = codec11.read(buffer);
                P12 result12 = codec12.read(buffer);
                P13 result13 = codec13.read(buffer);
                P14 result14 = codec14.read(buffer);
                P15 result15 = codec15.read(buffer);
                P16 result16 = codec16.read(buffer);
                P17 result17 = codec17.read(buffer);
                P18 result18 = codec18.read(buffer);
                P19 result19 = codec19.read(buffer);
                P20 result20 = codec20.read(buffer);
                return supplier.apply(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13, result14, result15, result16, result17, result18, result19, result20);
            }
        };
    }
}
