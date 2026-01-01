package totemofluck.config;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import java.util.List;

public class ModConfig {
    // 只存储效果ID字符串（避免静态初始化时访问注册表）
    private static final List<String> BUFF_POOL_IDS = List.of(
            "minecraft:speed",
            "minecraft:haste",
            "minecraft:strength",
            "minecraft:jump_boost",
            "minecraft:regeneration",
            "minecraft:fire_resistance",
            "minecraft:water_breathing",
            "minecraft:night_vision",
            "minecraft:luck"
    );

    // 在运行时动态获取Holder（安全）
    public static List<Holder<MobEffect>> getBuffPool() {
        return BUFF_POOL_IDS.stream()
                .map(id -> BuiltInRegistries.MOB_EFFECT
                        .getHolderOrThrow(ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation.parse(id))).getDelegate())
                .toList();
    }

    public static final int BUFF_SECONDS = 60;           // 行为buff持续时间（秒）
    public static final int BUFF_CHANCE_PERCENT = 10;    // 行为触发buff的几率

    // ========== 新增：幸运图腾复活配置 ==========

    // 1. 复活相关配置
    public static final boolean ENABLE_TOTEM_RESURRECTION = true;          // 是否启用图腾复活
    public static final boolean FULL_HEALTH_RESURRECTION = true;           // 是否满血复活（true=满血，false=1点血）
    public static final boolean RESTORE_HUNGER = true;                     // 是否恢复饥饿值
    public static final boolean CHECK_UNBLOCKABLE_DAMAGE = true;           // 是否检查不可阻挡伤害（虚空、/kill等）

    // 2. 复活效果配置（单位：刻，20刻=1秒）
    public static final int RESURRECTION_REGENERATION_DURATION = 1200;     // 再生效果持续时间（60秒）
    public static final int RESURRECTION_REGENERATION_AMPLIFIER = 1;       // 再生效果等级（0=1级，1=2级）

    public static final int RESURRECTION_ABSORPTION_DURATION = 2400;       // 吸收效果持续时间（120秒）
    public static final int RESURRECTION_ABSORPTION_AMPLIFIER = 4;         // 吸收效果等级（4=5级，20点吸收）

    public static final int RESURRECTION_FIRE_RESISTANCE_DURATION = 1600;  // 防火效果持续时间（80秒）

    public static final int RESURRECTION_RESISTANCE_DURATION = 600;        // 抗性提升持续时间（30秒）
    public static final int RESURRECTION_RESISTANCE_AMPLIFIER = 0;         // 抗性提升等级（0=1级）

    public static final int RESURRECTION_LUCK_DURATION = 3600;             // 幸运效果持续时间（180秒）
    public static final int RESURRECTION_LUCK_AMPLIFIER = 0;               // 幸运效果等级（0=1级）

    // 3. 额外复活效果
    public static final boolean ADD_NIGHT_VISION = true;                   // 是否添加夜视效果
    public static final int NIGHT_VISION_DURATION = 100;                   // 夜视效果持续时间（5秒）

    // 4. 音效和视觉效果
    public static final boolean PLAY_RESURRECTION_SOUND = true;            // 是否播放复活音效
    public static final float RESURRECTION_SOUND_VOLUME = 1.0F;            // 音效音量
    public static final float RESURRECTION_SOUND_PITCH = 1.0F;             // 音调

    public static final boolean SHOW_RESURRECTION_PARTICLES = true;        // 是否显示额外粒子效果
    public static final int PARTICLE_COUNT = 20;                           // 粒子数量

    // 5. 兼容性配置
    public static final boolean WORK_IN_HARD_DIFFICULTY = true;            // 是否在困难难度工作
    public static final boolean CONSUME_TOTEM_IN_CREATIVE = false;         // 创造模式是否消耗图腾

    // 6. 冷却时间（防止滥用）
    public static final int RESURRECTION_COOLDOWN_SECONDS = 0;             // 复活冷却时间（秒，0=无冷却）

    // ========== 工具方法 ==========

    /**
     * 获取复活效果的完整持续时间（秒）
     */
    public static int getResurrectionEffectDurationSeconds() {
        int maxDuration = Math.max(
                RESURRECTION_REGENERATION_DURATION,
                Math.max(
                        RESURRECTION_ABSORPTION_DURATION,
                        Math.max(
                                RESURRECTION_FIRE_RESISTANCE_DURATION,
                                RESURRECTION_LUCK_DURATION
                        )
                )
        );
        return maxDuration / 20; // 转换为秒
    }

    /**
     * 获取行为buff的持续时间（刻）
     */
    public static int getBuffDurationTicks() {
        return BUFF_SECONDS * 20;
    }

    /**
     * 获取复活冷却时间（刻）
     */
    public static int getResurrectionCooldownTicks() {
        return RESURRECTION_COOLDOWN_SECONDS * 20;
    }

    /**
     * 检查是否可以触发行为buff
     */
    public static boolean shouldTriggerBuff() {
        return Math.random() * 100 < BUFF_CHANCE_PERCENT;
    }

    /**
     * 获取有效的复活效果列表
     */
    public static List<Holder<MobEffect>> getResurrectionEffects() {
        return List.of(
                MobEffects.REGENERATION,
                MobEffects.ABSORPTION,
                MobEffects.FIRE_RESISTANCE,
                MobEffects.DAMAGE_RESISTANCE,
                MobEffects.LUCK
        );
    }
}
