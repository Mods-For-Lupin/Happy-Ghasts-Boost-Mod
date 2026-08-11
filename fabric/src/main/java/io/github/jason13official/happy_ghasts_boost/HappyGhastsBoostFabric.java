package io.github.jason13official.happy_ghasts_boost;

import io.github.jason13official.happy_ghasts_boost.impl.common.EarlyLoadConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class HappyGhastsBoostFabric implements ModInitializer {

  @Override
  public void onInitialize() {

    HappyGhastsBoost.init();

    ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(HappyGhastsBoost.identifier(Constants.MOD_ID), new ResourceReloadListener());
  }

  public static class ResourceReloadListener extends SimplePreparableReloadListener<Void> {

    @Override
    public String getName() {
      return HappyGhastsBoost.identifier(Constants.MOD_ID).toString();
    }

    @Override
    protected void apply(Void unused, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      EarlyLoadConfig.createOrLoadConfiguration();
    }

    @Override
    protected Void prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      return null;
    }
  }
}
