package totemofluck;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import totemofluck.client.TotemAnimationRenderer;
import totemofluck.item.ModItems;
import totemofluck.event.DeathEventHandler;
import totemofluck.event.PlayerEventHandler;
import totemofluck.util.TotemHelper;

@Mod(TotemOfLuck.MOD_ID)
public class TotemOfLuck {
    public static final String MOD_ID = "totemofluck";

    public TotemOfLuck(IEventBus modBus) {
        ModItems.ITEMS.register(modBus);
        modBus.addListener(this::addCreative);

        NeoForge.EVENT_BUS.register(new DeathEventHandler());
        NeoForge.EVENT_BUS.register(new PlayerEventHandler());

        System.out.println("=== TOTEM MOD INITIALIZED ===");
        System.out.println("Is client: " + (FMLEnvironment.dist == Dist.CLIENT));

        if (FMLEnvironment.dist == Dist.CLIENT) {
            startClientRenderThread();
        }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent e) {
        if (e.getTabKey() == CreativeModeTabs.COMBAT) {
            e.accept(ModItems.TOTEM_OF_LUCK.get());
        }
    }

    private void startClientRenderThread() {
        new Thread(() -> {
            Minecraft mc = Minecraft.getInstance();
            while (mc.isRunning()) {
                try {
                    Thread.sleep(50); // 20 FPS 检查
                    if (mc.player != null && mc.player.deathTime > 0) {
                        // ✅ 每 50ms 检查并渲染
                        totemofluck.client.TotemAnimationRenderer.renderTotemAnimation(mc.player);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static net.minecraft.resources.ResourceLocation modLoc(String path) {
        return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
