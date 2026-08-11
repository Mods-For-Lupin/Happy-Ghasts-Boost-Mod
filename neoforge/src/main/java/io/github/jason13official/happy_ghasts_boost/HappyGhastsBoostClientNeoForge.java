package io.github.jason13official.happy_ghasts_boost;

import io.github.jason13official.happy_ghasts_boost.platform.Services;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class HappyGhastsBoostClientNeoForge {

  public HappyGhastsBoostClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> HappyGhastsBoostClient.init());

    NeoForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {
      if (event.getEntity() == Minecraft.getInstance().player) {
        if (Services.PLATFORM.isDevelopmentEnvironment()) {
          Constants.LOG.info("Resetting tracked value upon joining a world.");
        }
        HappyGhastsBoostClient.topSpeedOnGhast = 0.0f;
      }
    });

    NeoForge.EVENT_BUS.addListener((Consumer<RenderGuiEvent.Post>) event -> {
      HappyGhastsBoostClient.renderHudLayer(event.getGuiGraphics(), event.getPartialTick());
    });
  }
}
