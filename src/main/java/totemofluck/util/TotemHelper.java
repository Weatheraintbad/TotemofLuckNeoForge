package totemofluck.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import totemofluck.item.ModItems;

public class TotemHelper {
    public static boolean hasLuckyTotem(Player player) {
        return findLuckyTotem(player) != null;
    }

    public static ItemStack findLuckyTotem(Player player) {
        if (player.getMainHandItem().is(ModItems.TOTEM_OF_LUCK.get())) {
            return player.getMainHandItem();
        }
        if (player.getOffhandItem().is(ModItems.TOTEM_OF_LUCK.get())) {
            return player.getOffhandItem();
        }
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(ModItems.TOTEM_OF_LUCK.get())) {
                return stack;
            }
        }
        return null;
    }
}
