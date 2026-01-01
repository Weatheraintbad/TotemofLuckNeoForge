package totemofluck.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import totemofluck.util.TotemHelper;
import totemofluck.item.ModItems;

public class TotemEffectRenderer {

    public static void renderTotemEffect(int playerId) {
        Minecraft mc = Minecraft.getInstance();
        Entity entity = mc.level.getEntity(playerId);

        if (!(entity instanceof Player player)) return;

        ItemStack totemStack = TotemHelper.findLuckyTotem(player);
        if (totemStack == null) return;

        mc.level.addParticle(
                net.minecraft.core.particles.ParticleTypes.TOTEM_OF_UNDYING,
                player.getX(), player.getY() + 1, player.getZ(),
                0, 0, 0
        );

        System.out.println("=== TOTEM PARTICLE RENDERED ===");
    }
}
