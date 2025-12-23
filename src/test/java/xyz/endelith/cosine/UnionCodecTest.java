package xyz.endelith.cosine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.netty.buffer.Unpooled;
import xyz.endelith.cosine.codec.Codec;
import xyz.endelith.cosine.codec.StructCodec;
import xyz.endelith.cosine.stream.StreamCodec;
import xyz.endelith.cosine.transcoder.JsonTranscoder;

public class UnionCodecTest {

    public interface ConsumeEffects {
    
        public enum Type {
            APPLY_EFFECTS,
            REMOVE_EFFECTS,
            PLAY_SOUND,
            TELEPORT
        }
        
        public record ApplyEffects(List<String> effects) implements ConsumeEffects {
            public static final StructCodec<ApplyEffects> CODEC = StructCodec.of(
                "effects", Codec.STRING.list(), ApplyEffects::effects,
                ApplyEffects::new
            );

            public static final StreamCodec<ApplyEffects> STREAM_CODEC = StreamCodec.of(
                StreamCodec.STRING.list(), ApplyEffects::effects,
                ApplyEffects::new
            );
        }

        record RemoveEffects(List<String> effects) implements ConsumeEffects {
            public static final StructCodec<RemoveEffects> CODEC = StructCodec.of(
                "effects", Codec.STRING.list(), RemoveEffects::effects,
                RemoveEffects::new
            );

            public static final StreamCodec<RemoveEffects> STREAM_CODEC = StreamCodec.of(
                StreamCodec.STRING.list(), RemoveEffects::effects,
                RemoveEffects::new
            );
        }

        record PlaySound(String sound, float range) implements ConsumeEffects { 
            public static final StructCodec<PlaySound> CODEC = StructCodec.of(
                "sound", Codec.STRING, PlaySound::sound,
                "range", Codec.FLOAT, PlaySound::range,
                PlaySound::new
            );

            public static final StreamCodec<PlaySound> STREAM_CODEC = StreamCodec.of(
                StreamCodec.STRING, PlaySound::sound,
                StreamCodec.FLOAT, PlaySound::range,
                PlaySound::new
            );
        }

        record Teleport(float radius) implements ConsumeEffects {
            public static final StructCodec<Teleport> CODEC = StructCodec.of(
                "radius", Codec.FLOAT, Teleport::radius,
                Teleport::new
            );

            public static final StreamCodec<Teleport> STREAM_CODEC = StreamCodec.of(
                StreamCodec.FLOAT, Teleport::radius,
                Teleport::new
            );
        }
     
        Codec<ConsumeEffects> CODEC = Codec.enumOf(Type.class).union(
            type -> switch (type) {
                case APPLY_EFFECTS -> ApplyEffects.CODEC;
                case REMOVE_EFFECTS -> RemoveEffects.CODEC;
                case PLAY_SOUND -> PlaySound.CODEC;
                case TELEPORT -> Teleport.CODEC;
            },
            consumeEffect -> {
                if (consumeEffect instanceof ApplyEffects) return Type.APPLY_EFFECTS;
                if (consumeEffect instanceof RemoveEffects) return Type.REMOVE_EFFECTS;
                if (consumeEffect instanceof PlaySound) return Type.PLAY_SOUND;
                if (consumeEffect instanceof Teleport) return Type.TELEPORT;
                throw new IllegalArgumentException("Unknown ConsumeEffect: " + consumeEffect);
            }
        );

        StreamCodec<ConsumeEffects> STREAM_CODEC = StreamCodec.enumOf(Type.class).union(
            type -> switch(type) {
                case APPLY_EFFECTS -> ApplyEffects.STREAM_CODEC;
                case REMOVE_EFFECTS -> RemoveEffects.STREAM_CODEC;
                case PLAY_SOUND -> PlaySound.STREAM_CODEC;
                case TELEPORT -> Teleport.STREAM_CODEC;
                default -> throw new IllegalStateException("Unknown type: " + type);
            },
            consumeEffect -> {
                if (consumeEffect instanceof ApplyEffects) return Type.APPLY_EFFECTS;
                if (consumeEffect instanceof RemoveEffects) return Type.REMOVE_EFFECTS;
                if (consumeEffect instanceof PlaySound) return Type.PLAY_SOUND;
                if (consumeEffect instanceof Teleport) return Type.TELEPORT;
                throw new IllegalArgumentException("Unknown ConsumeEffect: " + consumeEffect);
            }
        );
    }

    @Test
    void testUnionCodec() {
        var tp = new ConsumeEffects.ApplyEffects(List.of("minecraft:speed", "minecraft:help"));
        var encoded = ConsumeEffects.ApplyEffects.CODEC.encode(JsonTranscoder.INSTANCE, tp);
        var decoded = ConsumeEffects.ApplyEffects.CODEC.decode(JsonTranscoder.INSTANCE, encoded);
        assertEquals(tp, decoded);
    }

    @Test
    public void testUnionStreamCodec() {
        var tp = new ConsumeEffects.ApplyEffects(List.of("minecraft:speed", "minecraft:help"));
        var buffer = Unpooled.buffer();
        ConsumeEffects.STREAM_CODEC.write(buffer, tp);
        var decoded = ConsumeEffects.STREAM_CODEC.read(buffer);
        assertEquals(tp, decoded);
    }
}
