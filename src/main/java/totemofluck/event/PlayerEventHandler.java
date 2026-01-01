package totemofluck.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import totemofluck.api.LuckyTotemAPI;

public class PlayerEventHandler {

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent e) {
        if (!e.getLevel().isClientSide()) {
            LuckyTotemAPI.tryApplyBuff(e.getPlayer());
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent e) {
        if (!e.getEntity().level().isClientSide() && !e.getTarget().isAlive()) {
            LuckyTotemAPI.tryApplyBuff(e.getEntity());
        }
    }

    @SubscribeEvent
    public void onRightClickItem(net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickItem e) {
        if (!e.getLevel().isClientSide()) {
            LuckyTotemAPI.tryApplyBuff(e.getEntity());
        }
    }
}
