package io.github.jason13official.happy_ghasts_boost;

import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class HappyGhastsBoostClientForge {

  public HappyGhastsBoostClientForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> HappyGhastsBoostClient.init());
  }
}
