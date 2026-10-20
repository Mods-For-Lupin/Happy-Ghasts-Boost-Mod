package io.github.jason13official.happy_ghasts_boost;

import io.github.jason13official.happy_ghasts_boost.platform.Services;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class HappyGhastsBoostClientForge {

  public HappyGhastsBoostClientForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> HappyGhastsBoostClient.init());

    MinecraftForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {
      if (event.getEntity() == Minecraft.getInstance().player) {
        if (Services.PLATFORM.isDevelopmentEnvironment()) {
          Constants.LOG.info("Resetting tracked value upon joining a world.");
        }
        HappyGhastsBoostClient.topSpeedOnGhast = 0.0f;
      }
    });

    MinecraftForge.EVENT_BUS.addListener((Consumer<RenderGuiEvent.Post>) event -> {
      HappyGhastsBoostClient.renderHudLayer(event.getGuiGraphics());
    });
  }
}
