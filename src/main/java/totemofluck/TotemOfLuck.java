package totemofluck;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import totemofluck.item.ModItems;
import totemofluck.event.DeathEventHandler;
import totemofluck.event.PlayerEventHandler;
import totemofluck.network.TotemOfLuckPacket;

@Mod(TotemOfLuck.MOD_ID)
public class TotemOfLuck {
    public static final String MOD_ID = "totemofluck";

    public TotemOfLuck(IEventBus modBus) {
        ModItems.ITEMS.register(modBus);
        modBus.addListener(this::addCreative);
        modBus.addListener(this::registerPayloadHandler);

        NeoForge.EVENT_BUS.register(new DeathEventHandler());
        NeoForge.EVENT_BUS.register(new PlayerEventHandler());
    }

    private void addCreative(BuildCreativeModeTabContentsEvent e) {
        if (e.getTabKey() == CreativeModeTabs.COMBAT) {
            e.accept(ModItems.TOTEM_OF_LUCK.get());
        }
    }

    private void registerPayloadHandler(RegisterPayloadHandlersEvent event) {
        event.registrar(MOD_ID)
                .playToClient(TotemOfLuckPacket.TYPE, TotemOfLuckPacket.STREAM_CODEC,
                        TotemOfLuckPacket.Handler::handle);
    }

    public static net.minecraft.resources.ResourceLocation modLoc(String path) {
        return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}