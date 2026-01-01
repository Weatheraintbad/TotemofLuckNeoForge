package totemofluck.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.Difficulty;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import totemofluck.config.ModConfig;
import totemofluck.item.ModItems;
import totemofluck.util.TotemHelper;
import totemofluck.client.TotemAnimationRenderer;

public class DeathEventHandler {
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent e) {
        if (!(e.getEntity() instanceof Player player) || !ModConfig.ENABLE_TOTEM_RESURRECTION) {
            return;
        }

        var server = ServerLifecycleHooks.getCurrentServer();
        if (server != null && server.isSameThread() && player instanceof FakePlayer) {
            return;
        }

        if (tryResurrect(player, e.getSource())) {
            e.setCanceled(true);
        }
    }

    private boolean tryResurrect(Player player, DamageSource src) {
        if (!ModConfig.WORK_IN_HARD_DIFFICULTY && player.level().getDifficulty() == Difficulty.HARD) {
            return false;
        }

        if (ModConfig.CHECK_UNBLOCKABLE_DAMAGE && isUnblockable(src)) {
            return false;
        }

        ItemStack totem = TotemHelper.findLuckyTotem(player);
        if (totem == null) return false;

        applyEffects(player, totem);
        return true;
    }

    private boolean isUnblockable(DamageSource src) {
        String msgId = src.getMsgId();
        return "outOfWorld".equals(msgId) || "genericKill".equals(msgId);
    }

    private void applyEffects(Player player, ItemStack totem) {
        player.setHealth(ModConfig.FULL_HEALTH_RESURRECTION ? player.getMaxHealth() : 1.0F);
        player.removeAllEffects();
        player.clearFire();

        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION,
                ModConfig.RESURRECTION_REGENERATION_DURATION,
                ModConfig.RESURRECTION_REGENERATION_AMPLIFIER));

        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION,
                ModConfig.RESURRECTION_ABSORPTION_DURATION,
                ModConfig.RESURRECTION_ABSORPTION_AMPLIFIER));

        if (ModConfig.RESURRECTION_FIRE_RESISTANCE_DURATION > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,
                    ModConfig.RESURRECTION_FIRE_RESISTANCE_DURATION, 0));
        }

        if (ModConfig.RESURRECTION_RESISTANCE_DURATION > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,
                    ModConfig.RESURRECTION_RESISTANCE_DURATION,
                    ModConfig.RESURRECTION_RESISTANCE_AMPLIFIER));
        }

        if (ModConfig.ADD_NIGHT_VISION && ModConfig.NIGHT_VISION_DURATION > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,
                    ModConfig.NIGHT_VISION_DURATION, 0));
        }

        if (ModConfig.RESTORE_HUNGER) {
            player.getFoodData().setFoodLevel(20);
            player.getFoodData().setSaturation(5.0F);
        }

        if (ModConfig.PLAY_RESURRECTION_SOUND) {
            player.level().playSound(null, player.blockPosition(),
                    SoundEvents.TOTEM_USE, SoundSource.PLAYERS,
                    ModConfig.RESURRECTION_SOUND_VOLUME,
                    ModConfig.RESURRECTION_SOUND_PITCH);
        }

        if (player.level().isClientSide) {
            // 直接调用，不依赖任何事件
            totemofluck.client.TotemAnimationRenderer.renderTotemAnimation(player);
        } else {
            // 服务端：通过实体数据标记
            player.getPersistentData().putBoolean("totem_animation", true);
        }

        if (totem != null) totem.shrink(1);
    }
}
