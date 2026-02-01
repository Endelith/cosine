package xyz.endelith.cosine.codec;

import org.jspecify.annotations.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer;
import xyz.endelith.cosine.transcoder.Transcoder;

public final class ComponentCodecs {

    private static final NBTComponentSerializer SERIALIZER = NBTComponentSerializer.nbt();

    private static final JSONComponentSerializer JSON_SERIALIZER = JSONComponentSerializer.json();

    public static final Codec<Component> COMPONENT = new Codec<>() {

        @Override
        public <D> Component decode(Transcoder<D> transcoder, D value) {
            BinaryTag tag = Codec.NBT.decode(transcoder, value);
            return SERIALIZER.deserialize(tag);
        }

        @Override
        public <D> D encode(Transcoder<D> transcoder, @Nullable Component value) {
            if (value == null) {
                return transcoder.encodeNull();
            }

            BinaryTag tag = SERIALIZER.serialize(value);
            return Codec.NBT.encode(transcoder, tag);
        }
    };

    public static final Codec<Component> JSON_COMPONENT = new Codec<>() {

        @Override
        public <D> Component decode(Transcoder<D> transcoder, D value) {
            String json = value.toString();
            return JSON_SERIALIZER.deserialize(json);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <D> D encode(Transcoder<D> transcoder, @Nullable Component value) {
            if (value == null) {
                return transcoder.encodeNull();
            }

            String json = JSON_SERIALIZER.serialize(value);
            JsonElement element = JsonParser.parseString(json);
            return (D) element;
        }
    };

    public static final Codec<Style> STYLE = new Codec<>() {

        @Override
        public <D> Style decode(Transcoder<D> transcoder, D value) {
            BinaryTag tag = Codec.NBT.decode(transcoder, value);
            if (tag instanceof CompoundBinaryTag compound) {
                return SERIALIZER.deserializeStyle(compound);
            } else {
                throw new IllegalArgumentException("The encoded tag must be of compound type to decode it to a style");
            }
        }

        @Override
        public <D> D encode(Transcoder<D> transcoder, @Nullable Style value) {
            if (value == null) {
                return transcoder.encodeNull();
            }

            BinaryTag tag = SERIALIZER.serializeStyle(value);
            return Codec.NBT.encode(transcoder, tag);
        } 
    };
}
