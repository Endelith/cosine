package xyz.endelith.cosine.codec;

import java.util.function.Function;

import xyz.endelith.cosine.transcoder.Transcoder;

public interface StructCodec<R> extends Codec<R> {

    <T> T encodeToMap(Transcoder<T> transcoder, R value, Transcoder.VirtualMapBuilder<T> map);

    <T> R decodeFromMap(Transcoder<T> transcoder, Transcoder.VirtualMap<T> map);

    @Override
    default <T> T encode(Transcoder<T> transcoder, R value) {
        return encodeToMap(transcoder, value, transcoder.encodeMap());
    }

    @Override
    default <D> R decode(Transcoder<D> transcoder, D value) {
        return decodeFromMap(transcoder, transcoder.decodeMap(value));
    }

    String INLINE = "_inline_";

    static <R> StructCodec<R> of(Supplier0<R> constructor) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(Transcoder<T> transcoder, R value, Transcoder.VirtualMapBuilder<T> map) {
                return transcoder.emptyMap();
            }

            @Override
            public <T> R decodeFromMap(Transcoder<T> transcoder, Transcoder.VirtualMap<T> map) {
                return constructor.get();
            }
        };
    }

    static <P1, R> StructCodec<R> of(
            String name1, Codec<P1> codec1, Function<R, P1> getter1,
            Function1<P1, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(Transcoder<T> transcoder, R value, Transcoder.VirtualMapBuilder<T> map) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(Transcoder<T> transcoder, Transcoder.VirtualMap<T> map) {
                P1 r1 = get(transcoder, codec1, name1, map);
                return constructor.apply(r1);
            }
        };
    }

    static <P1, P2, R> StructCodec<R> of(
            String name1, Codec<P1> codec1, Function<R, P1> getter1,
            String name2, Codec<P2> codec2, Function<R, P2> getter2,
            Function2<P1, P2, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(Transcoder<T> transcoder, R value, Transcoder.VirtualMapBuilder<T> map) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(Transcoder<T> transcoder, Transcoder.VirtualMap<T> map) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                return constructor.apply(r1, r2);
            }
        };
    }

    static <P1, P2, P3, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            Function3<P1, P2, P3, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                return constructor.apply(r1, r2, r3);
            }
        };
    }


    static <P1, P2, P3, P4, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            Function4<P1, P2, P3, P4, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                return constructor.apply(r1, r2, r3, r4);
            }
        };
    }


    static <P1, P2, P3, P4, P5, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            Function5<P1, P2, P3, P4, P5, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                return constructor.apply(r1, r2, r3, r4, r5);
            }
        };
    }


    static <P1, P2, P3, P4, P5, P6, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            String name6,
            Codec<P6> codec6,
            Function<R, P6> getter6,
            Function6<P1, P2, P3, P4, P5, P6, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                put(transcoder, codec6, map, name6, getter6.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                P6 r6 = get(transcoder, codec6, name6, map);
                return constructor.apply(r1, r2, r3, r4, r5, r6);
            }
        };
    }


    static <P1, P2, P3, P4, P5, P6, P7, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            String name6,
            Codec<P6> codec6,
            Function<R, P6> getter6,
            String name7,
            Codec<P7> codec7,
            Function<R, P7> getter7,
            Function7<P1, P2, P3, P4, P5, P6, P7, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                put(transcoder, codec6, map, name6, getter6.apply(value));
                put(transcoder, codec7, map, name7, getter7.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                P6 r6 = get(transcoder, codec6, name6, map);
                P7 r7 = get(transcoder, codec7, name7, map);
                return constructor.apply(r1, r2, r3, r4, r5, r6, r7);
            }
        };
    }


    static <P1, P2, P3, P4, P5, P6, P7, P8, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            String name6,
            Codec<P6> codec6,
            Function<R, P6> getter6,
            String name7,
            Codec<P7> codec7,
            Function<R, P7> getter7,
            String name8,
            Codec<P8> codec8,
            Function<R, P8> getter8,
            Function8<P1, P2, P3, P4, P5, P6, P7, P8, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                put(transcoder, codec6, map, name6, getter6.apply(value));
                put(transcoder, codec7, map, name7, getter7.apply(value));
                put(transcoder, codec8, map, name8, getter8.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                P6 r6 = get(transcoder, codec6, name6, map);
                P7 r7 = get(transcoder, codec7, name7, map);
                P8 r8 = get(transcoder, codec8, name8, map);
                return constructor.apply(r1, r2, r3, r4, r5, r6, r7, r8);
            }
        };
    }


    static <P1, P2, P3, P4, P5, P6, P7, P8, P9, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            String name6,
            Codec<P6> codec6,
            Function<R, P6> getter6,
            String name7,
            Codec<P7> codec7,
            Function<R, P7> getter7,
            String name8,
            Codec<P8> codec8,
            Function<R, P8> getter8,
            String name9,
            Codec<P9> codec9,
            Function<R, P9> getter9,
            Function9<P1, P2, P3, P4, P5, P6, P7, P8, P9, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                put(transcoder, codec6, map, name6, getter6.apply(value));
                put(transcoder, codec7, map, name7, getter7.apply(value));
                put(transcoder, codec8, map, name8, getter8.apply(value));
                put(transcoder, codec9, map, name9, getter9.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                P6 r6 = get(transcoder, codec6, name6, map);
                P7 r7 = get(transcoder, codec7, name7, map);
                P8 r8 = get(transcoder, codec8, name8, map);
                P9 r9 = get(transcoder, codec9, name9, map);
                return constructor.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9);
            }
        };
    }


    static <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            String name6,
            Codec<P6> codec6,
            Function<R, P6> getter6,
            String name7,
            Codec<P7> codec7,
            Function<R, P7> getter7,
            String name8,
            Codec<P8> codec8,
            Function<R, P8> getter8,
            String name9,
            Codec<P9> codec9,
            Function<R, P9> getter9,
            String name10,
            Codec<P10> codec10,
            Function<R, P10> getter10,
            Function10<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                put(transcoder, codec6, map, name6, getter6.apply(value));
                put(transcoder, codec7, map, name7, getter7.apply(value));
                put(transcoder, codec8, map, name8, getter8.apply(value));
                put(transcoder, codec9, map, name9, getter9.apply(value));
                put(transcoder, codec10, map, name10, getter10.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                P6 r6 = get(transcoder, codec6, name6, map);
                P7 r7 = get(transcoder, codec7, name7, map);
                P8 r8 = get(transcoder, codec8, name8, map);
                P9 r9 = get(transcoder, codec9, name9, map);
                P10 r10 = get(transcoder, codec10, name10, map);
                return constructor.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
            }
        };
    }


    static <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            String name6,
            Codec<P6> codec6,
            Function<R, P6> getter6,
            String name7,
            Codec<P7> codec7,
            Function<R, P7> getter7,
            String name8,
            Codec<P8> codec8,
            Function<R, P8> getter8,
            String name9,
            Codec<P9> codec9,
            Function<R, P9> getter9,
            String name10,
            Codec<P10> codec10,
            Function<R, P10> getter10,
            String name11,
            Codec<P11> codec11,
            Function<R, P11> getter11,
            Function11<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                put(transcoder, codec6, map, name6, getter6.apply(value));
                put(transcoder, codec7, map, name7, getter7.apply(value));
                put(transcoder, codec8, map, name8, getter8.apply(value));
                put(transcoder, codec9, map, name9, getter9.apply(value));
                put(transcoder, codec10, map, name10, getter10.apply(value));
                put(transcoder, codec11, map, name11, getter11.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                P6 r6 = get(transcoder, codec6, name6, map);
                P7 r7 = get(transcoder, codec7, name7, map);
                P8 r8 = get(transcoder, codec8, name8, map);
                P9 r9 = get(transcoder, codec9, name9, map);
                P10 r10 = get(transcoder, codec10, name10, map);
                P11 r11 = get(transcoder, codec11, name11, map);
                return constructor.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);
            }
        };
    }


    static <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            String name6,
            Codec<P6> codec6,
            Function<R, P6> getter6,
            String name7,
            Codec<P7> codec7,
            Function<R, P7> getter7,
            String name8,
            Codec<P8> codec8,
            Function<R, P8> getter8,
            String name9,
            Codec<P9> codec9,
            Function<R, P9> getter9,
            String name10,
            Codec<P10> codec10,
            Function<R, P10> getter10,
            String name11,
            Codec<P11> codec11,
            Function<R, P11> getter11,
            String name12,
            Codec<P12> codec12,
            Function<R, P12> getter12,
            Function12<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                put(transcoder, codec6, map, name6, getter6.apply(value));
                put(transcoder, codec7, map, name7, getter7.apply(value));
                put(transcoder, codec8, map, name8, getter8.apply(value));
                put(transcoder, codec9, map, name9, getter9.apply(value));
                put(transcoder, codec10, map, name10, getter10.apply(value));
                put(transcoder, codec11, map, name11, getter11.apply(value));
                put(transcoder, codec12, map, name12, getter12.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                P6 r6 = get(transcoder, codec6, name6, map);
                P7 r7 = get(transcoder, codec7, name7, map);
                P8 r8 = get(transcoder, codec8, name8, map);
                P9 r9 = get(transcoder, codec9, name9, map);
                P10 r10 = get(transcoder, codec10, name10, map);
                P11 r11 = get(transcoder, codec11, name11, map);
                P12 r12 = get(transcoder, codec12, name12, map);
                return constructor.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12);
            }
        };
    }


    static <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            String name6,
            Codec<P6> codec6,
            Function<R, P6> getter6,
            String name7,
            Codec<P7> codec7,
            Function<R, P7> getter7,
            String name8,
            Codec<P8> codec8,
            Function<R, P8> getter8,
            String name9,
            Codec<P9> codec9,
            Function<R, P9> getter9,
            String name10,
            Codec<P10> codec10,
            Function<R, P10> getter10,
            String name11,
            Codec<P11> codec11,
            Function<R, P11> getter11,
            String name12,
            Codec<P12> codec12,
            Function<R, P12> getter12,
            String name13,
            Codec<P13> codec13,
            Function<R, P13> getter13,
            Function13<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                put(transcoder, codec6, map, name6, getter6.apply(value));
                put(transcoder, codec7, map, name7, getter7.apply(value));
                put(transcoder, codec8, map, name8, getter8.apply(value));
                put(transcoder, codec9, map, name9, getter9.apply(value));
                put(transcoder, codec10, map, name10, getter10.apply(value));
                put(transcoder, codec11, map, name11, getter11.apply(value));
                put(transcoder, codec12, map, name12, getter12.apply(value));
                put(transcoder, codec13, map, name13, getter13.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                P6 r6 = get(transcoder, codec6, name6, map);
                P7 r7 = get(transcoder, codec7, name7, map);
                P8 r8 = get(transcoder, codec8, name8, map);
                P9 r9 = get(transcoder, codec9, name9, map);
                P10 r10 = get(transcoder, codec10, name10, map);
                P11 r11 = get(transcoder, codec11, name11, map);
                P12 r12 = get(transcoder, codec12, name12, map);
                P13 r13 = get(transcoder, codec13, name13, map);
                return constructor.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);
            }
        };
    }


    static <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            String name6,
            Codec<P6> codec6,
            Function<R, P6> getter6,
            String name7,
            Codec<P7> codec7,
            Function<R, P7> getter7,
            String name8,
            Codec<P8> codec8,
            Function<R, P8> getter8,
            String name9,
            Codec<P9> codec9,
            Function<R, P9> getter9,
            String name10,
            Codec<P10> codec10,
            Function<R, P10> getter10,
            String name11,
            Codec<P11> codec11,
            Function<R, P11> getter11,
            String name12,
            Codec<P12> codec12,
            Function<R, P12> getter12,
            String name13,
            Codec<P13> codec13,
            Function<R, P13> getter13,
            String name14,
            Codec<P14> codec14,
            Function<R, P14> getter14,
            Function14<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                put(transcoder, codec6, map, name6, getter6.apply(value));
                put(transcoder, codec7, map, name7, getter7.apply(value));
                put(transcoder, codec8, map, name8, getter8.apply(value));
                put(transcoder, codec9, map, name9, getter9.apply(value));
                put(transcoder, codec10, map, name10, getter10.apply(value));
                put(transcoder, codec11, map, name11, getter11.apply(value));
                put(transcoder, codec12, map, name12, getter12.apply(value));
                put(transcoder, codec13, map, name13, getter13.apply(value));
                put(transcoder, codec14, map, name14, getter14.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                P6 r6 = get(transcoder, codec6, name6, map);
                P7 r7 = get(transcoder, codec7, name7, map);
                P8 r8 = get(transcoder, codec8, name8, map);
                P9 r9 = get(transcoder, codec9, name9, map);
                P10 r10 = get(transcoder, codec10, name10, map);
                P11 r11 = get(transcoder, codec11, name11, map);
                P12 r12 = get(transcoder, codec12, name12, map);
                P13 r13 = get(transcoder, codec13, name13, map);
                P14 r14 = get(transcoder, codec14, name14, map);
                return constructor.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14);
            }
        };
    }


    static <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            String name6,
            Codec<P6> codec6,
            Function<R, P6> getter6,
            String name7,
            Codec<P7> codec7,
            Function<R, P7> getter7,
            String name8,
            Codec<P8> codec8,
            Function<R, P8> getter8,
            String name9,
            Codec<P9> codec9,
            Function<R, P9> getter9,
            String name10,
            Codec<P10> codec10,
            Function<R, P10> getter10,
            String name11,
            Codec<P11> codec11,
            Function<R, P11> getter11,
            String name12,
            Codec<P12> codec12,
            Function<R, P12> getter12,
            String name13,
            Codec<P13> codec13,
            Function<R, P13> getter13,
            String name14,
            Codec<P14> codec14,
            Function<R, P14> getter14,
            String name15,
            Codec<P15> codec15,
            Function<R, P15> getter15,
            Function15<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                put(transcoder, codec6, map, name6, getter6.apply(value));
                put(transcoder, codec7, map, name7, getter7.apply(value));
                put(transcoder, codec8, map, name8, getter8.apply(value));
                put(transcoder, codec9, map, name9, getter9.apply(value));
                put(transcoder, codec10, map, name10, getter10.apply(value));
                put(transcoder, codec11, map, name11, getter11.apply(value));
                put(transcoder, codec12, map, name12, getter12.apply(value));
                put(transcoder, codec13, map, name13, getter13.apply(value));
                put(transcoder, codec14, map, name14, getter14.apply(value));
                put(transcoder, codec15, map, name15, getter15.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                P6 r6 = get(transcoder, codec6, name6, map);
                P7 r7 = get(transcoder, codec7, name7, map);
                P8 r8 = get(transcoder, codec8, name8, map);
                P9 r9 = get(transcoder, codec9, name9, map);
                P10 r10 = get(transcoder, codec10, name10, map);
                P11 r11 = get(transcoder, codec11, name11, map);
                P12 r12 = get(transcoder, codec12, name12, map);
                P13 r13 = get(transcoder, codec13, name13, map);
                P14 r14 = get(transcoder, codec14, name14, map);
                P15 r15 = get(transcoder, codec15, name15, map);
                return constructor.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15);
            }
        };
    }


    static <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            String name6,
            Codec<P6> codec6,
            Function<R, P6> getter6,
            String name7,
            Codec<P7> codec7,
            Function<R, P7> getter7,
            String name8,
            Codec<P8> codec8,
            Function<R, P8> getter8,
            String name9,
            Codec<P9> codec9,
            Function<R, P9> getter9,
            String name10,
            Codec<P10> codec10,
            Function<R, P10> getter10,
            String name11,
            Codec<P11> codec11,
            Function<R, P11> getter11,
            String name12,
            Codec<P12> codec12,
            Function<R, P12> getter12,
            String name13,
            Codec<P13> codec13,
            Function<R, P13> getter13,
            String name14,
            Codec<P14> codec14,
            Function<R, P14> getter14,
            String name15,
            Codec<P15> codec15,
            Function<R, P15> getter15,
            String name16,
            Codec<P16> codec16,
            Function<R, P16> getter16,
            Function16<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                put(transcoder, codec6, map, name6, getter6.apply(value));
                put(transcoder, codec7, map, name7, getter7.apply(value));
                put(transcoder, codec8, map, name8, getter8.apply(value));
                put(transcoder, codec9, map, name9, getter9.apply(value));
                put(transcoder, codec10, map, name10, getter10.apply(value));
                put(transcoder, codec11, map, name11, getter11.apply(value));
                put(transcoder, codec12, map, name12, getter12.apply(value));
                put(transcoder, codec13, map, name13, getter13.apply(value));
                put(transcoder, codec14, map, name14, getter14.apply(value));
                put(transcoder, codec15, map, name15, getter15.apply(value));
                put(transcoder, codec16, map, name16, getter16.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                P6 r6 = get(transcoder, codec6, name6, map);
                P7 r7 = get(transcoder, codec7, name7, map);
                P8 r8 = get(transcoder, codec8, name8, map);
                P9 r9 = get(transcoder, codec9, name9, map);
                P10 r10 = get(transcoder, codec10, name10, map);
                P11 r11 = get(transcoder, codec11, name11, map);
                P12 r12 = get(transcoder, codec12, name12, map);
                P13 r13 = get(transcoder, codec13, name13, map);
                P14 r14 = get(transcoder, codec14, name14, map);
                P15 r15 = get(transcoder, codec15, name15, map);
                P16 r16 = get(transcoder, codec16, name16, map);
                return constructor.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16);
            }
        };
    }


    static <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            String name6,
            Codec<P6> codec6,
            Function<R, P6> getter6,
            String name7,
            Codec<P7> codec7,
            Function<R, P7> getter7,
            String name8,
            Codec<P8> codec8,
            Function<R, P8> getter8,
            String name9,
            Codec<P9> codec9,
            Function<R, P9> getter9,
            String name10,
            Codec<P10> codec10,
            Function<R, P10> getter10,
            String name11,
            Codec<P11> codec11,
            Function<R, P11> getter11,
            String name12,
            Codec<P12> codec12,
            Function<R, P12> getter12,
            String name13,
            Codec<P13> codec13,
            Function<R, P13> getter13,
            String name14,
            Codec<P14> codec14,
            Function<R, P14> getter14,
            String name15,
            Codec<P15> codec15,
            Function<R, P15> getter15,
            String name16,
            Codec<P16> codec16,
            Function<R, P16> getter16,
            String name17,
            Codec<P17> codec17,
            Function<R, P17> getter17,
            Function17<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                put(transcoder, codec6, map, name6, getter6.apply(value));
                put(transcoder, codec7, map, name7, getter7.apply(value));
                put(transcoder, codec8, map, name8, getter8.apply(value));
                put(transcoder, codec9, map, name9, getter9.apply(value));
                put(transcoder, codec10, map, name10, getter10.apply(value));
                put(transcoder, codec11, map, name11, getter11.apply(value));
                put(transcoder, codec12, map, name12, getter12.apply(value));
                put(transcoder, codec13, map, name13, getter13.apply(value));
                put(transcoder, codec14, map, name14, getter14.apply(value));
                put(transcoder, codec15, map, name15, getter15.apply(value));
                put(transcoder, codec16, map, name16, getter16.apply(value));
                put(transcoder, codec17, map, name17, getter17.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                P6 r6 = get(transcoder, codec6, name6, map);
                P7 r7 = get(transcoder, codec7, name7, map);
                P8 r8 = get(transcoder, codec8, name8, map);
                P9 r9 = get(transcoder, codec9, name9, map);
                P10 r10 = get(transcoder, codec10, name10, map);
                P11 r11 = get(transcoder, codec11, name11, map);
                P12 r12 = get(transcoder, codec12, name12, map);
                P13 r13 = get(transcoder, codec13, name13, map);
                P14 r14 = get(transcoder, codec14, name14, map);
                P15 r15 = get(transcoder, codec15, name15, map);
                P16 r16 = get(transcoder, codec16, name16, map);
                P17 r17 = get(transcoder, codec17, name17, map);
                return constructor.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17);
            }
        };
    }


    static <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            String name6,
            Codec<P6> codec6,
            Function<R, P6> getter6,
            String name7,
            Codec<P7> codec7,
            Function<R, P7> getter7,
            String name8,
            Codec<P8> codec8,
            Function<R, P8> getter8,
            String name9,
            Codec<P9> codec9,
            Function<R, P9> getter9,
            String name10,
            Codec<P10> codec10,
            Function<R, P10> getter10,
            String name11,
            Codec<P11> codec11,
            Function<R, P11> getter11,
            String name12,
            Codec<P12> codec12,
            Function<R, P12> getter12,
            String name13,
            Codec<P13> codec13,
            Function<R, P13> getter13,
            String name14,
            Codec<P14> codec14,
            Function<R, P14> getter14,
            String name15,
            Codec<P15> codec15,
            Function<R, P15> getter15,
            String name16,
            Codec<P16> codec16,
            Function<R, P16> getter16,
            String name17,
            Codec<P17> codec17,
            Function<R, P17> getter17,
            String name18,
            Codec<P18> codec18,
            Function<R, P18> getter18,
            Function18<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                put(transcoder, codec6, map, name6, getter6.apply(value));
                put(transcoder, codec7, map, name7, getter7.apply(value));
                put(transcoder, codec8, map, name8, getter8.apply(value));
                put(transcoder, codec9, map, name9, getter9.apply(value));
                put(transcoder, codec10, map, name10, getter10.apply(value));
                put(transcoder, codec11, map, name11, getter11.apply(value));
                put(transcoder, codec12, map, name12, getter12.apply(value));
                put(transcoder, codec13, map, name13, getter13.apply(value));
                put(transcoder, codec14, map, name14, getter14.apply(value));
                put(transcoder, codec15, map, name15, getter15.apply(value));
                put(transcoder, codec16, map, name16, getter16.apply(value));
                put(transcoder, codec17, map, name17, getter17.apply(value));
                put(transcoder, codec18, map, name18, getter18.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                P6 r6 = get(transcoder, codec6, name6, map);
                P7 r7 = get(transcoder, codec7, name7, map);
                P8 r8 = get(transcoder, codec8, name8, map);
                P9 r9 = get(transcoder, codec9, name9, map);
                P10 r10 = get(transcoder, codec10, name10, map);
                P11 r11 = get(transcoder, codec11, name11, map);
                P12 r12 = get(transcoder, codec12, name12, map);
                P13 r13 = get(transcoder, codec13, name13, map);
                P14 r14 = get(transcoder, codec14, name14, map);
                P15 r15 = get(transcoder, codec15, name15, map);
                P16 r16 = get(transcoder, codec16, name16, map);
                P17 r17 = get(transcoder, codec17, name17, map);
                P18 r18 = get(transcoder, codec18, name18, map);
                return constructor.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18);
            }
        };
    }


    static <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            String name6,
            Codec<P6> codec6,
            Function<R, P6> getter6,
            String name7,
            Codec<P7> codec7,
            Function<R, P7> getter7,
            String name8,
            Codec<P8> codec8,
            Function<R, P8> getter8,
            String name9,
            Codec<P9> codec9,
            Function<R, P9> getter9,
            String name10,
            Codec<P10> codec10,
            Function<R, P10> getter10,
            String name11,
            Codec<P11> codec11,
            Function<R, P11> getter11,
            String name12,
            Codec<P12> codec12,
            Function<R, P12> getter12,
            String name13,
            Codec<P13> codec13,
            Function<R, P13> getter13,
            String name14,
            Codec<P14> codec14,
            Function<R, P14> getter14,
            String name15,
            Codec<P15> codec15,
            Function<R, P15> getter15,
            String name16,
            Codec<P16> codec16,
            Function<R, P16> getter16,
            String name17,
            Codec<P17> codec17,
            Function<R, P17> getter17,
            String name18,
            Codec<P18> codec18,
            Function<R, P18> getter18,
            String name19,
            Codec<P19> codec19,
            Function<R, P19> getter19,
            Function19<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                put(transcoder, codec6, map, name6, getter6.apply(value));
                put(transcoder, codec7, map, name7, getter7.apply(value));
                put(transcoder, codec8, map, name8, getter8.apply(value));
                put(transcoder, codec9, map, name9, getter9.apply(value));
                put(transcoder, codec10, map, name10, getter10.apply(value));
                put(transcoder, codec11, map, name11, getter11.apply(value));
                put(transcoder, codec12, map, name12, getter12.apply(value));
                put(transcoder, codec13, map, name13, getter13.apply(value));
                put(transcoder, codec14, map, name14, getter14.apply(value));
                put(transcoder, codec15, map, name15, getter15.apply(value));
                put(transcoder, codec16, map, name16, getter16.apply(value));
                put(transcoder, codec17, map, name17, getter17.apply(value));
                put(transcoder, codec18, map, name18, getter18.apply(value));
                put(transcoder, codec19, map, name19, getter19.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                P6 r6 = get(transcoder, codec6, name6, map);
                P7 r7 = get(transcoder, codec7, name7, map);
                P8 r8 = get(transcoder, codec8, name8, map);
                P9 r9 = get(transcoder, codec9, name9, map);
                P10 r10 = get(transcoder, codec10, name10, map);
                P11 r11 = get(transcoder, codec11, name11, map);
                P12 r12 = get(transcoder, codec12, name12, map);
                P13 r13 = get(transcoder, codec13, name13, map);
                P14 r14 = get(transcoder, codec14, name14, map);
                P15 r15 = get(transcoder, codec15, name15, map);
                P16 r16 = get(transcoder, codec16, name16, map);
                P17 r17 = get(transcoder, codec17, name17, map);
                P18 r18 = get(transcoder, codec18, name18, map);
                P19 r19 = get(transcoder, codec19, name19, map);
                return constructor.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19);
            }
        };
    }


    static <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, R> StructCodec<R> of(
            String name1,
            Codec<P1> codec1,
            Function<R, P1> getter1,
            String name2,
            Codec<P2> codec2,
            Function<R, P2> getter2,
            String name3,
            Codec<P3> codec3,
            Function<R, P3> getter3,
            String name4,
            Codec<P4> codec4,
            Function<R, P4> getter4,
            String name5,
            Codec<P5> codec5,
            Function<R, P5> getter5,
            String name6,
            Codec<P6> codec6,
            Function<R, P6> getter6,
            String name7,
            Codec<P7> codec7,
            Function<R, P7> getter7,
            String name8,
            Codec<P8> codec8,
            Function<R, P8> getter8,
            String name9,
            Codec<P9> codec9,
            Function<R, P9> getter9,
            String name10,
            Codec<P10> codec10,
            Function<R, P10> getter10,
            String name11,
            Codec<P11> codec11,
            Function<R, P11> getter11,
            String name12,
            Codec<P12> codec12,
            Function<R, P12> getter12,
            String name13,
            Codec<P13> codec13,
            Function<R, P13> getter13,
            String name14,
            Codec<P14> codec14,
            Function<R, P14> getter14,
            String name15,
            Codec<P15> codec15,
            Function<R, P15> getter15,
            String name16,
            Codec<P16> codec16,
            Function<R, P16> getter16,
            String name17,
            Codec<P17> codec17,
            Function<R, P17> getter17,
            String name18,
            Codec<P18> codec18,
            Function<R, P18> getter18,
            String name19,
            Codec<P19> codec19,
            Function<R, P19> getter19,
            String name20,
            Codec<P20> codec20,
            Function<R, P20> getter20,
            Function20<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, R> constructor
    ) {
        return new StructCodec<>() {
            @Override
            public <T> T encodeToMap(
                    Transcoder<T> transcoder,
                    R value,
                    Transcoder.VirtualMapBuilder<T> map
            ) {
                put(transcoder, codec1, map, name1, getter1.apply(value));
                put(transcoder, codec2, map, name2, getter2.apply(value));
                put(transcoder, codec3, map, name3, getter3.apply(value));
                put(transcoder, codec4, map, name4, getter4.apply(value));
                put(transcoder, codec5, map, name5, getter5.apply(value));
                put(transcoder, codec6, map, name6, getter6.apply(value));
                put(transcoder, codec7, map, name7, getter7.apply(value));
                put(transcoder, codec8, map, name8, getter8.apply(value));
                put(transcoder, codec9, map, name9, getter9.apply(value));
                put(transcoder, codec10, map, name10, getter10.apply(value));
                put(transcoder, codec11, map, name11, getter11.apply(value));
                put(transcoder, codec12, map, name12, getter12.apply(value));
                put(transcoder, codec13, map, name13, getter13.apply(value));
                put(transcoder, codec14, map, name14, getter14.apply(value));
                put(transcoder, codec15, map, name15, getter15.apply(value));
                put(transcoder, codec16, map, name16, getter16.apply(value));
                put(transcoder, codec17, map, name17, getter17.apply(value));
                put(transcoder, codec18, map, name18, getter18.apply(value));
                put(transcoder, codec19, map, name19, getter19.apply(value));
                put(transcoder, codec20, map, name20, getter20.apply(value));
                return map.build();
            }

            @Override
            public <T> R decodeFromMap(
                    Transcoder<T> transcoder,
                    Transcoder.VirtualMap<T> map
            ) {
                P1 r1 = get(transcoder, codec1, name1, map);
                P2 r2 = get(transcoder, codec2, name2, map);
                P3 r3 = get(transcoder, codec3, name3, map);
                P4 r4 = get(transcoder, codec4, name4, map);
                P5 r5 = get(transcoder, codec5, name5, map);
                P6 r6 = get(transcoder, codec6, name6, map);
                P7 r7 = get(transcoder, codec7, name7, map);
                P8 r8 = get(transcoder, codec8, name8, map);
                P9 r9 = get(transcoder, codec9, name9, map);
                P10 r10 = get(transcoder, codec10, name10, map);
                P11 r11 = get(transcoder, codec11, name11, map);
                P12 r12 = get(transcoder, codec12, name12, map);
                P13 r13 = get(transcoder, codec13, name13, map);
                P14 r14 = get(transcoder, codec14, name14, map);
                P15 r15 = get(transcoder, codec15, name15, map);
                P16 r16 = get(transcoder, codec16, name16, map);
                P17 r17 = get(transcoder, codec17, name17, map);
                P18 r18 = get(transcoder, codec18, name18, map);
                P19 r19 = get(transcoder, codec19, name19, map);
                P20 r20 = get(transcoder, codec20, name20, map);
                return constructor.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20);
            }
        };
    }


    @SuppressWarnings("unchecked")
    static <D, T> D put(
            Transcoder<D> transcoder,
            Codec<T> codec,
            Transcoder.VirtualMapBuilder<D> map,
            String key,
            T value
    ) {

        if (INLINE.equals(key)) {
            Codec<T> encodeCodec;

            if (codec instanceof OptionalCodec<?> oc) {
                encodeCodec = (Codec<T>) oc.inner();
            } else if (codec instanceof DefaultCodec<?> dc) {
                encodeCodec = (Codec<T>) dc.inner();
            } else if (codec instanceof RecursiveCodec<?> rc) {
                encodeCodec = (Codec<T>) rc.self();
            } else {
                encodeCodec = codec;
            }

            if (!(encodeCodec instanceof StructCodec<?> sc)) {
                throw new Codec.EncodingException("Provided codec for inline " + key + " is not StructCodec");
            }

            return ((StructCodec<T>) sc).encodeToMap(transcoder, value, map);
        }

        D result = codec.encode(transcoder, value);
        map.put(key, result);
        return result;
    }

    @SuppressWarnings("unchecked")
    static <D, T> T get(
            Transcoder<D> transcoder,
            Codec<T> codec,
            String key,
            Transcoder.VirtualMap<D> map
    ) {

        if (INLINE.equals(key)) {
            Codec<T> decodeCodec;

            if (codec instanceof OptionalCodec<?> oc) {
                decodeCodec = (Codec<T>) oc.inner();
            } else if (codec instanceof DefaultCodec<?> dc) {
                decodeCodec = (Codec<T>) dc.inner();
            } else if (codec instanceof RecursiveCodec<?> rc) {
                decodeCodec = (Codec<T>) rc.self();
            } else {
                decodeCodec = codec;
            }

            if (!(decodeCodec instanceof StructCodec<?> sc)) {
                throw new Codec.EncodingException("Provided codec for inline " + key + " is not StructCodec");
            }

            try {
                return ((StructCodec<T>) sc).decodeFromMap(transcoder, map);
            } catch (Throwable t) {
                if (codec instanceof DefaultCodec<?> dc) {
                    return (T) dc.def();
                }
                if (codec instanceof OptionalCodec<?>) {
                    return null;
                }
                throw t;
            }
        }

        if (codec instanceof DefaultCodec<?> dc && !map.hasValue(key)) {
            return (T) dc.def();
        }

        if (codec instanceof OptionalCodec<?> && !map.hasValue(key)) {
            return null;
        }

        return codec.decode(transcoder, map.getValue(key));
    }

    @FunctionalInterface
    interface Supplier0<R> {
        R get();
    }

    @FunctionalInterface
    interface Function1<P1, R> {
        R apply(P1 p1);
    }

    @FunctionalInterface
    interface Function2<P1, P2, R> {
        R apply(P1 p1, P2 p2);
    }

    @FunctionalInterface
    interface Function3<P1, P2, P3, R> {
        R apply(P1 p1, P2 p2, P3 p3);
    }
    
    @FunctionalInterface
    interface Function4<P1, P2, P3, P4, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4);
    }
    
    @FunctionalInterface
    interface Function5<P1, P2, P3, P4, P5, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5);
    }
    
    @FunctionalInterface
    interface Function6<P1, P2, P3, P4, P5, P6, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6);
    }
    
    @FunctionalInterface
    interface Function7<P1, P2, P3, P4, P5, P6, P7, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7);
    }
    
    @FunctionalInterface
    interface Function8<P1, P2, P3, P4, P5, P6, P7, P8, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8);
    }
    
    @FunctionalInterface
    interface Function9<P1, P2, P3, P4, P5, P6, P7, P8, P9, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9);
    }
    
    @FunctionalInterface
    interface Function10<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9, P10 p10);
    }
    
    @FunctionalInterface
    interface Function11<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9, P10 p10, P11 p11);
    }
    
    @FunctionalInterface
    interface Function12<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9, P10 p10, P11 p11, P12 p12);
    }
    
    @FunctionalInterface
    interface Function13<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9, P10 p10, P11 p11, P12 p12, P13 p13);
    }
    
    @FunctionalInterface
    interface Function14<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9, P10 p10, P11 p11, P12 p12, P13 p13, P14 p14);
    }
    
    @FunctionalInterface
    interface Function15<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9, P10 p10, P11 p11, P12 p12, P13 p13, P14 p14, P15 p15);
    }
    
    @FunctionalInterface
    interface Function16<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9, P10 p10, P11 p11, P12 p12, P13 p13, P14 p14, P15 p15, P16 p16);
    }
    
    @FunctionalInterface
    interface Function17<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9, P10 p10, P11 p11, P12 p12, P13 p13, P14 p14, P15 p15, P16 p16, P17 p17);
    }
    
    @FunctionalInterface
    interface Function18<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9, P10 p10, P11 p11, P12 p12, P13 p13, P14 p14, P15 p15, P16 p16, P17 p17, P18 p18);
    }
    
    @FunctionalInterface
    interface Function19<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9, P10 p10, P11 p11, P12 p12, P13 p13, P14 p14, P15 p15, P16 p16, P17 p17, P18 p18, P19 p19);
    }
        
    @FunctionalInterface
    interface Function20<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, R> {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7, P8 p8, P9 p9, P10 p10, P11 p11, P12 p12, P13 p13, P14 p14, P15 p15, P16 p16, P17 p17, P18 p18, P19 p19, P20 p20);
    }    
}
