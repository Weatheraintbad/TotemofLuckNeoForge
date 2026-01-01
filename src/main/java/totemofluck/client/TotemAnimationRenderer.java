package totemofluck.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import totemofluck.item.ModItems;

public class TotemAnimationRenderer {
    public static void renderTotemAnimation(Player player) {
        if (player.deathTime <= 0) return;

        Minecraft mc = Minecraft.getInstance();
        GuiGraphics gui = new GuiGraphics(mc, mc.renderBuffers().bufferSource());

        int x = mc.getWindow().getGuiScaledWidth() / 2;
        int y = mc.getWindow().getGuiScaledHeight() / 2;

        gui.pose().pushPose();
        gui.pose().translate(x, y, 0);
        gui.pose().scale(30, 30, 1);

        gui.renderItem(ModItems.TOTEM_OF_LUCK.get().getDefaultInstance(), -8, -8);

        gui.pose().popPose();

        System.out.println("=== TOTEM RENDERED AT " + x + "," + y + " ===");
    }
}
