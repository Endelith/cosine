package xyz.endelith.cosine;

import org.junit.jupiter.api.Test;

import xyz.endelith.cosine.codec.Codec;
import xyz.endelith.cosine.codec.StructCodec;
import xyz.endelith.cosine.transcoder.JsonTranscoder;

public class DefaultCodecTest {

    public record Player(String name, boolean isBanned) {
        public static final StructCodec<Player> CODEC = StructCodec.of(
            "name", Codec.STRING, Player::name,
            "isbanned", Codec.BOOLEAN.defaultValue(false), Player::isBanned,
            Player::new
        );
    }

    @Test
    void testDefault() {
        var player = new Player("SnowyQuest", false);
        var encoded = Player.CODEC.encode(JsonTranscoder.INSTANCE, player);
        assert(!encoded.toString().contains("isBanned"));
    }
}
