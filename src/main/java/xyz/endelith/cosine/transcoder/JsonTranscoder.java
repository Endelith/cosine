package xyz.endelith.cosine.transcoder;

import java.util.Collection;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public final class JsonTranscoder implements Transcoder<JsonElement> {

    public static final JsonTranscoder INSTANCE = new JsonTranscoder();

    private JsonTranscoder() {
    }

    @Override
    public JsonElement encodeNull() {
        return JsonNull.INSTANCE;
    }

    @Override
    public JsonElement encodeBoolean(boolean value) {
        return new JsonPrimitive(value);
    }

    @Override
    public JsonElement encodeByte(byte value) {
        return new JsonPrimitive(value);
    }

    @Override
    public JsonElement encodeShort(short value) {
        return new JsonPrimitive(value);
    }

    @Override
    public JsonElement encodeInt(int value) {
        return new JsonPrimitive(value);
    }

    @Override
    public JsonElement encodeLong(long value) {
        return new JsonPrimitive(value);
    }

    @Override
    public JsonElement encodeFloat(float value) {
        return new JsonPrimitive(value);
    }

    @Override
    public JsonElement encodeDouble(double value) {
        return new JsonPrimitive(value);
    }

    @Override
    public JsonElement encodeString(String value) {
        return new JsonPrimitive(value);
    }

    @Override
    public ListBuilder<JsonElement> encodeList(int size) {
        JsonArray jsonList = new JsonArray();

        return new ListBuilder<>() {
            @Override
            public ListBuilder<JsonElement> add(JsonElement value) {
                jsonList.add(value);
                return this;
            }

            @Override
            public JsonElement build() {
                return jsonList;
            }
        };
    }

    @Override
    public VirtualMapBuilder<JsonElement> encodeMap() {
        JsonObject jsonMap = new JsonObject();

        return new VirtualMapBuilder<>() {

            @Override
            public VirtualMapBuilder<JsonElement> put(JsonElement key, JsonElement value) {
                return put(key.getAsString(), value);
            }

            @Override
            public VirtualMapBuilder<JsonElement> put(String key, JsonElement value) {
                jsonMap.add(key, value);
                return this;
            }

            @Override
            public JsonElement build() {
                return jsonMap;
            }
        };
    }

    @Override
    public VirtualMap<JsonElement> decodeMap(JsonElement value) {
        if (!(value instanceof JsonObject jsonObject)) {
            throw new IllegalArgumentException("value is not JsonObject!");
        }

        return new VirtualMap<>() {

            @Override
            public Collection<String> getKeys() {
                return jsonObject.keySet();
            }

            @Override
            public boolean hasValue(String key) {
                return jsonObject.has(key);
            }

            @Override
            public JsonElement getValue(String key) {
                return jsonObject.get(key);
            }
        };
    }

    @Override
    public List<JsonElement> decodeList(JsonElement value) {
        if (!(value instanceof JsonArray jsonArray)) {
            throw new IllegalArgumentException("value is not JsonArray");
        }
        return jsonArray.asList();
    }

    @Override
    public String decodeString(JsonElement value) {
        return value.getAsString();
    }

    @Override
    public double decodeDouble(JsonElement value) {
        return value.getAsDouble();
    }

    @Override
    public float decodeFloat(JsonElement value) {
        return value.getAsFloat();
    }

    @Override
    public long decodeLong(JsonElement value) {
        return value.getAsLong();
    }

    @Override
    public int decodeInt(JsonElement value) {
        return value.getAsInt();
    }

    @Override
    public short decodeShort(JsonElement value) {
        return value.getAsShort();
    }

    @Override
    public byte decodeByte(JsonElement value) {
        return value.getAsByte();
    }

    @Override
    public boolean decodeBoolean(JsonElement value) {
        return value.getAsBoolean();
    }
}
