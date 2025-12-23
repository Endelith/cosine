package xyz.endelith.cosine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.endelith.cosine.stream.StreamCodec;

public class StreamCodecTest {

    public record Player(String username, UUID uuid, PlayerData data) {

        public static final StreamCodec<Player> STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, Player::username,
            StreamCodec.UUID, Player::uuid,
            PlayerData.STREAM_CODEC.optional(), Player::data,
            Player::new
        );

        public record PlayerData(boolean isBanned, String banReason, Rank rank) {

            public static final StreamCodec<PlayerData> STREAM_CODEC = StreamCodec.of(
                StreamCodec.BOOLEAN, PlayerData::isBanned,
                StreamCodec.STRING.optional(), PlayerData::banReason,
                StreamCodec.enumOf(Rank.class), PlayerData::rank,
                PlayerData::new
            );
        }

        public enum Rank {
            ADMIN,
            PLAYER,
            MODERATOR
        }
    }

    @Test
    void testWriteRead() {
        Player.PlayerData playerData = new Player.PlayerData(true, null, Player.Rank.ADMIN);
        Player player = new Player("SnowyQuest", UUID.randomUUID(), playerData);

        ByteBuf buffer = Unpooled.buffer();
        Player.STREAM_CODEC.write(buffer, player);

        Player decoded = Player.STREAM_CODEC.read(buffer);
        assertEquals(player, decoded);
    }
}
