package xyz.endelith.cosine.transcoder;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.ByteArrayBinaryTag;
import net.kyori.adventure.nbt.ByteBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.DoubleBinaryTag;
import net.kyori.adventure.nbt.EndBinaryTag;
import net.kyori.adventure.nbt.FloatBinaryTag;
import net.kyori.adventure.nbt.IntArrayBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.LongArrayBinaryTag;
import net.kyori.adventure.nbt.LongBinaryTag;
import net.kyori.adventure.nbt.NumberBinaryTag;
import net.kyori.adventure.nbt.ShortBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;

public final class NbtTranscoder implements Transcoder<BinaryTag> {

    public static final NbtTranscoder INSTANCE = new NbtTranscoder();

    private NbtTranscoder() {
    }

    @Override
    public BinaryTag encodeNull() {
        return EndBinaryTag.endBinaryTag();
    }

    @Override
    public BinaryTag encodeBoolean(boolean value) {
        return value ? ByteBinaryTag.ONE : ByteBinaryTag.ZERO;
    }

    @Override
    public boolean decodeBoolean(BinaryTag value) {
        if (value instanceof NumberBinaryTag number) {
            return number.byteValue() != 0;
        }
        throw new IllegalArgumentException("Not a boolean: " + value);
    }

    @Override
    public BinaryTag encodeByte(byte value) {
        if (value == 0) return ByteBinaryTag.ZERO;
        if (value == 1) return ByteBinaryTag.ONE;
        return ByteBinaryTag.byteBinaryTag(value);
    }

    @Override
    public byte decodeByte(BinaryTag value) {
        if (value instanceof NumberBinaryTag number) {
            return number.byteValue();
        }
        throw new IllegalArgumentException("Not a byte: " + value);
    }

    @Override
    public BinaryTag encodeShort(short value) {
        return ShortBinaryTag.shortBinaryTag(value);
    }

    @Override
    public short decodeShort(BinaryTag value) {
        if (value instanceof NumberBinaryTag number) {
            return number.shortValue();
        }
        throw new IllegalArgumentException("Not a short: " + value);
    }

    @Override
    public BinaryTag encodeInt(int value) {
        return IntBinaryTag.intBinaryTag(value);
    }

    @Override
    public int decodeInt(BinaryTag value) {
        if (value instanceof NumberBinaryTag number) {
            return number.intValue();
        }
        throw new IllegalArgumentException("Not an int: " + value);
    }

    @Override
    public BinaryTag encodeLong(long value) {
        return LongBinaryTag.longBinaryTag(value);
    }

    @Override
    public long decodeLong(BinaryTag value) {
        if (value instanceof NumberBinaryTag number) {
            return number.longValue();
        }
        throw new IllegalArgumentException("Not a long: " + value);
    }

    @Override
    public BinaryTag encodeFloat(float value) {
        return FloatBinaryTag.floatBinaryTag(value);
    }

    @Override
    public float decodeFloat(BinaryTag value) {
        if (value instanceof NumberBinaryTag number) {
            return number.floatValue();
        }
        throw new IllegalArgumentException("Not a float: " + value);
    }

    @Override
    public BinaryTag encodeDouble(double value) {
        return DoubleBinaryTag.doubleBinaryTag(value);
    }

    @Override
    public double decodeDouble(BinaryTag value) {
        if (value instanceof NumberBinaryTag number) {
            return number.doubleValue();
        }
        throw new IllegalArgumentException("Not a double: " + value);
    }

    @Override
    public BinaryTag encodeString(String value) {
        return StringBinaryTag.stringBinaryTag(value);
    }

    @Override
    public String decodeString(BinaryTag value) {
        if (value instanceof StringBinaryTag string) {
            return string.value();
        }
        throw new IllegalArgumentException("Not a string: " + value);
    }

    @Override
    public ListBuilder<BinaryTag> encodeList(int size) {
        final ListBinaryTag.Builder<BinaryTag> builder =
                ListBinaryTag.heterogeneousListBinaryTag();

        return new ListBuilder<>() {
            @Override
            public ListBuilder<BinaryTag> add(BinaryTag value) {
                builder.add(value);
                return this;
            }

            @Override
            public BinaryTag build() {
                return builder.build();
            }
        };
    }

    @Override
    public List<BinaryTag> decodeList(BinaryTag value) {
        if (!(value instanceof ListBinaryTag wrapped)) {
            throw new IllegalArgumentException("Not a list: " + value);
        }

        final ListBinaryTag list = wrapped.unwrapHeterogeneity();
        return new AbstractList<>() {
            @Override
            public BinaryTag get(int index) {
                return list.get(index);
            }

            @Override
            public int size() {
                return list.size();
            }
        };
    }

    @Override
    public VirtualMapBuilder<BinaryTag> encodeMap() {
        final CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();

        return new VirtualMapBuilder<>() {
            @Override
            public VirtualMapBuilder<BinaryTag> put(BinaryTag key, BinaryTag value) {
                if (key instanceof StringBinaryTag string && !(value instanceof EndBinaryTag)) {
                    builder.put(string.value(), value);
                }
                return this;
            }

            @Override
            public VirtualMapBuilder<BinaryTag> put(String key, BinaryTag value) {
                if (!(value instanceof EndBinaryTag)) {
                    builder.put(key, value);
                }
                return this;
            }

            @Override
            public BinaryTag build() {
                return builder.build();
            }
        };
    }

    @Override
    public VirtualMap<BinaryTag> decodeMap(BinaryTag value) {
        if (!(value instanceof CompoundBinaryTag compound)) {
            throw new IllegalArgumentException("Not a map/compound: " + value);
        }

        return new VirtualMap<>() {
            @Override
            public Collection<String> getKeys() {
                return compound.keySet();
            }

            @Override
            public boolean hasValue(String key) {
                return compound.get(key) != null;
            }

            @Override
            public BinaryTag getValue(String key) {
                BinaryTag tag = compound.get(key);
                if (tag == null) {
                    throw new IllegalArgumentException("No such key: " + key);
                }
                return tag;
            }
        };
    }

    @Override
    public <O> O convertTo(Transcoder<O> target, BinaryTag value) {
        return switch (value) {
            case EndBinaryTag _ -> target.encodeNull();
            case ByteBinaryTag byteTag -> target.encodeByte(byteTag.byteValue());
            case ShortBinaryTag shortTag -> target.encodeShort(shortTag.shortValue());
            case IntBinaryTag intTag -> target.encodeInt(intTag.intValue());
            case LongBinaryTag longTag -> target.encodeLong(longTag.longValue());
            case FloatBinaryTag floatTag -> target.encodeFloat(floatTag.floatValue());
            case DoubleBinaryTag doubleTag -> target.encodeDouble(doubleTag.doubleValue());
            case StringBinaryTag stringTag -> target.encodeString(stringTag.value());
            case ByteArrayBinaryTag byteArrayTag -> target.encodeByteArray(byteArrayTag.value());
            case IntArrayBinaryTag intArrayTag -> target.encodeIntArray(intArrayTag.value());
            case LongArrayBinaryTag longArrayTag -> target.encodeLongArray(longArrayTag.value());
            case ListBinaryTag listTag -> {
                ListBinaryTag unwrapped = listTag.unwrapHeterogeneity();
                Transcoder.ListBuilder<O> list = target.encodeList(unwrapped.size());
                for (BinaryTag element : unwrapped) {
                    list.add(convertTo(target, element));
                }
                yield list.build();
            }
            case CompoundBinaryTag compoundTag -> {
                Transcoder.VirtualMapBuilder<O> map = target.encodeMap();
                for (Map.Entry<String, ? extends BinaryTag> entry : compoundTag) {
                    map.put(entry.getKey(), convertTo(target, entry.getValue()));
                }
                yield map.build();
            }
            default -> throw new IllegalArgumentException("Unsupported NBT type: " + value);
        };
    }
}
