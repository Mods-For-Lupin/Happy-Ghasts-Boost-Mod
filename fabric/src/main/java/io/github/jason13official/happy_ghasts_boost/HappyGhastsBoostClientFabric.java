package io.github.jason13official.happy_ghasts_boost;

import net.fabricmc.api.ClientModInitializer;

public class HappyGhastsBoostClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    HappyGhastsBoostClient.init();
  }
}
