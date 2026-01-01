package totemofluck.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import totemofluck.TotemOfLuck;
import totemofluck.item.ModItems;

public record TotemOfLuckPacket(int playerId) implements CustomPacketPayload {
    public static final Type<TotemOfLuckPacket> TYPE = new Type<>(TotemOfLuck.modLoc("totem_effect"));

    public static final StreamCodec<FriendlyByteBuf, TotemOfLuckPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> buf.writeInt(packet.playerId),
            (buf) -> new TotemOfLuckPacket(buf.readInt())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler {
        public static void handle(TotemOfLuckPacket packet, IPayloadContext context) {
            context.enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                if (mc.level == null) return;

                Entity entity = mc.level.getEntity(packet.playerId());
                if (entity instanceof Player player) {
                    ItemStack totemStack = new ItemStack(ModItems.TOTEM_OF_LUCK.get());

                    mc.gameRenderer.displayItemActivation(totemStack);

                    for (int i = 0; i < 100; i++) {
                        double offsetX = (player.getRandom().nextDouble() - 0.5) * 2.0;
                        double offsetY = player.getRandom().nextDouble() * 2.0;
                        double offsetZ = (player.getRandom().nextDouble() - 0.5) * 2.0;

                        mc.level.addParticle(
                                net.minecraft.core.particles.ParticleTypes.TOTEM_OF_UNDYING,
                                player.getX() + offsetX,
                                player.getY() + offsetY,
                                player.getZ() + offsetZ,
                                0, 0.1, 0
                        );
                    }

                    for (int i = 0; i < 30; i++) {
                        double offsetX = (player.getRandom().nextDouble() - 0.5) * 3.0;
                        double offsetY = player.getRandom().nextDouble() * 3.0;
                        double offsetZ = (player.getRandom().nextDouble() - 0.5) * 3.0;

                        mc.level.addParticle(
                                net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                                player.getX() + offsetX,
                                player.getY() + offsetY,
                                player.getZ() + offsetZ,
                                0, 0.05, 0
                        );
                    }
                }
            });
        }
    }
}