package io.github.jason13official.happy_ghasts_boost;

import io.github.jason13official.happy_ghasts_boost.platform.Services;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;

public class HappyGhastsBoostClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    HappyGhastsBoostClient.init();

    ClientEntityEvents.ENTITY_LOAD.register((entity, level) -> {

      if (entity == Minecraft.getInstance().player) {
        if (Services.PLATFORM.isDevelopmentEnvironment()) {
          Constants.LOG.info("Resetting tracked value upon joining a world.");
        }
        HappyGhastsBoostClient.topSpeedOnGhast = 0.0f;
      }
    });

    HudElementRegistry.addLast(HappyGhastsBoost.identifier("display"), HappyGhastsBoostClient::renderHudLayer);
  }
}
