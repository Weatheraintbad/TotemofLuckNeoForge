package totemofluck.api;

import totemofluck.config.ModConfig;
import totemofluck.util.TotemHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;

public class LuckyTotemAPI {
    public static void tryApplyBuff(Player player) {
        if (!TotemHelper.hasLuckyTotem(player)) return;

        if (ModConfig.shouldTriggerBuff()) {
            var effect = ModConfig.getBuffPool().get(player.getRandom().nextInt(ModConfig.getBuffPool().size()));
            player.addEffect(new MobEffectInstance(effect,
                    ModConfig.getBuffDurationTicks(), 0));
        }
    }
}
