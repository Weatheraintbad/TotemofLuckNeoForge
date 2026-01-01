package totemofluck.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import totemofluck.api.LuckyTotemAPI;

public class PlayerEventHandler {

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent e) {
        // 只在服务端执行，避免客户端重复触发
        if (!e.getLevel().isClientSide()) {
            LuckyTotemAPI.tryApplyBuff(e.getPlayer());
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent e) {
        // 只在服务端执行，且目标死亡后才触发
        if (!e.getEntity().level().isClientSide() && !e.getTarget().isAlive()) {
            LuckyTotemAPI.tryApplyBuff(e.getEntity());
        }
    }

    @SubscribeEvent
    public void onRightClickItem(net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickItem e) {
        // 只在服务端执行
        if (!e.getLevel().isClientSide()) {
            LuckyTotemAPI.tryApplyBuff(e.getEntity());
        }
    }
}
