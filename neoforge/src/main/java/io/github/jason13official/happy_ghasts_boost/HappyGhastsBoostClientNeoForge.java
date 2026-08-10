package io.github.jason13official.happy_ghasts_boost;

import java.util.function.Consumer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class HappyGhastsBoostClientNeoForge {

  public HappyGhastsBoostClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> HappyGhastsBoostClient.init());
  }
}
