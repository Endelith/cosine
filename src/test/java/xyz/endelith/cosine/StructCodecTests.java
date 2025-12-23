package xyz.endelith.cosine;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import xyz.endelith.cosine.codec.Codec;
import xyz.endelith.cosine.codec.StructCodec;
import xyz.endelith.cosine.transcoder.JsonTranscoder;

public class StructCodecTests {

    public record Person(String name, PersonData data) {
        public static final StructCodec<Person> CODEC = StructCodec.of(
            "name", Codec.STRING, Person::name,
            "data", PersonData.CODEC, Person::data,
            Person::new
        );

        public record PersonData(int age, String license) {
            public static final StructCodec<PersonData> CODEC = StructCodec.of(
                "age", Codec.INT, PersonData::age,
                "license", Codec.STRING.optional(), PersonData::license,
                PersonData::new
            );
        }
    }

    public static class Empty {
        @Override
        public boolean equals(Object other) {
            return other instanceof Empty;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    public record SingleField(String name) {
        public static final StructCodec<SingleField> CODEC = StructCodec.of("name", Codec.STRING, SingleField::name, SingleField::new);
    }

    public record SingleFieldOptional(String name) {
        public static final StructCodec<SingleFieldOptional> CODEC = StructCodec.of("name", Codec.STRING.optional(), SingleFieldOptional::name, SingleFieldOptional::new);
    }

    public record SingleFieldDefault(String name) {
        public static final StructCodec<SingleFieldDefault> CODEC = StructCodec.of("name", Codec.STRING.defaultValue("SnowyQuest"), SingleFieldDefault::name, SingleFieldDefault::new);
    }

    public record InlineField(String name, InnerObject innerObject) {
        public static final StructCodec<InlineField> CODEC = StructCodec.of(
            "name", Codec.STRING, InlineField::name,
            StructCodec.INLINE, InnerObject.CODEC, InlineField::innerObject,
            InlineField::new
        );

        public record InnerObject(String value) {
            public static final StructCodec<InnerObject> CODEC = StructCodec.of("value", Codec.STRING, InnerObject::value, InnerObject::new);
        }
    }

    @Test
    void emptyCodecTest() {
        StructCodec<Empty> codec = StructCodec.of(Empty::new);
        Empty result = codec.decode(JsonTranscoder.INSTANCE, new JsonObject());
        assertEquals(new Empty(), result);
    }

    @Test
    void testSingleField() {
        SingleField result = SingleField.CODEC.decode(JsonTranscoder.INSTANCE, json("{\"name\": \"SnowyQuest\"}"));
        assertEquals(new SingleField("SnowyQuest"), result);
    }

    @Test
    void testSingleFieldMissing() {
        assertThrows(Exception.class, () -> SingleField.CODEC.decode(JsonTranscoder.INSTANCE, json("{}")));
    }

    @Test
    void testSingleFieldOptionalMissing() {
        assertDoesNotThrow(() -> {
            SingleFieldOptional result = SingleFieldOptional.CODEC.decode(JsonTranscoder.INSTANCE, json("{}"));
            assertEquals(new SingleFieldOptional(null), result);
        });
        assertDoesNotThrow(() -> {
            SingleFieldOptional result = SingleFieldOptional.CODEC.decode(JsonTranscoder.INSTANCE, json("{\"name\": \"SnowyQuest\"}"));
            assertEquals(new SingleFieldOptional("SnowyQuest"), result);
        });
    }

    @Test
    void testSingleFieldOptionalDefaultMissing() {
        assertDoesNotThrow(() -> {
            SingleFieldDefault result = SingleFieldDefault.CODEC.decode(JsonTranscoder.INSTANCE, json("{}"));
            assertEquals(new SingleFieldDefault("SnowyQuest"), result);
        });
    }

    @Test
    void testInlineField() {
        InlineField result = InlineField.CODEC.decode(JsonTranscoder.INSTANCE, json("{ \"name\": \"SnowyQuest\", \"value\": \"inner_value\"}"));
        assertEquals(new InlineField("SnowyQuest", new InlineField.InnerObject("inner_value")), result);

        JsonElement encodeResult = InlineField.CODEC.encode(JsonTranscoder.INSTANCE, new InlineField("SnowyQuest", new InlineField.InnerObject("inner_value")));
        assertEquals(json("{ \"name\": \"SnowyQuest\", \"value\": \"inner_value\"}"), encodeResult);
    }

    @Test
    void testEncodeDecode() {
        Person person = new Person("SnowyQuest", new Person.PersonData(69, null));

        JsonElement encoded = Person.CODEC.encode(JsonTranscoder.INSTANCE, person);
        Person decoded = Person.CODEC.decode(JsonTranscoder.INSTANCE, encoded);

        assertEquals(person, decoded);
    }

    private static JsonElement json(String key) {
        return JsonParser.parseString(key);
    }
}
