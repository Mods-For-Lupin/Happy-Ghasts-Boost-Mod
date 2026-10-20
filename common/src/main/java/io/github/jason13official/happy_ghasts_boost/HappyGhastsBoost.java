package io.github.jason13official.happy_ghasts_boost;

import io.github.jason13official.monolib.MonoLib;
import io.github.jason13official.monolib.impl.common.sailing.Sailing;
import net.minecraft.resources.ResourceLocation;

public class HappyGhastsBoost {

  public static void init() {

    Sailing.register(Constants.MOD_ID, MonoLib.createFilename(Constants.MOD_ID, "1.20.1", "1.2.0"));
  }

  public static ResourceLocation identifier(final String path) {
    return new ResourceLocation(Constants.MOD_ID, path);
  }
}
