package io.github.jason13official.happy_ghasts_boost;

import io.github.jason13official.happy_ghasts_boost.impl.common.EarlyLoadConfig;
import java.util.function.Consumer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

@Mod(Constants.MOD_ID)
public class HappyGhastsBoostNeoForge {

  public static IEventBus EVENT_BUS;

  public HappyGhastsBoostNeoForge(final IEventBus modEventBus) {

    EVENT_BUS = modEventBus;

    EVENT_BUS.addListener((Consumer<FMLCommonSetupEvent>) event -> HappyGhastsBoost.init());

    NeoForge.EVENT_BUS.addListener((Consumer<AddServerReloadListenersEvent>) event -> {
      event.addListener(HappyGhastsBoost.identifier(Constants.MOD_ID), new ResourceReloadListener());
    });

    if (FMLLoader.getCurrent().getDist() == Dist.CLIENT) {
      new HappyGhastsBoostClientNeoForge(EVENT_BUS);
    }
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