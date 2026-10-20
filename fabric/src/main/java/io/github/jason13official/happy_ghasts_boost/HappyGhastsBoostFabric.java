package io.github.jason13official.happy_ghasts_boost;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class HappyGhastsBoostFabric implements ModInitializer {

  @Override
  public void onInitialize() {

    HappyGhastsBoost.init();

    ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new ResourceReloadListener());
  }

  public static class ResourceReloadListener implements SimpleSynchronousResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
      return HappyGhastsBoost.identifier(Constants.MOD_ID);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
      // ModConfig.load(Services.PLATFORM.getConfigDirectory());
    }
  }
}
