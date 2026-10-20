package io.github.jason13official.happy_ghasts_boost;

import com.blackgear.vanillabackport.common.level.entities.mob.animal.happy_ghast.HappyGhast;
import io.github.jason13official.happy_ghasts_boost.api.common.IHappyGhastBoostDataHolder;
import io.github.jason13official.happy_ghasts_boost.impl.common.EarlyLoadConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;

public class HappyGhastsBoostClient {

  /// client-only (duh), used for onscreen rendering
  public static float topSpeedOnGhast = 0f;

  public static void init() {
  }

  public static void renderHudLayer(GuiGraphics guiGraphics) {

    LocalPlayer player = Minecraft.getInstance().player;

    if (player != null && player.isPassenger() && player.getVehicle() instanceof HappyGhast ghast) {

      IHappyGhastBoostDataHolder dataHolder = (IHappyGhastBoostDataHolder) ghast;

      float speed = Mth.sqrt((float) ((ghast.getDeltaMovement().x * ghast.getDeltaMovement().x) + (ghast.getDeltaMovement().z * ghast.getDeltaMovement().z)));

      if (EarlyLoadConfig.DISPLAY_BOOST_BAR.get() && speed >= 0.1 && dataHolder.happy_ghasts_boost$getForwardFlightDuration() > 0) {

        int xStart = guiGraphics.guiWidth() / 2 - 91;
        int yStart = guiGraphics.guiHeight() - 32 + 3;

        float jumpRidingScale = Math.min((float) (dataHolder.happy_ghasts_boost$getForwardFlightDuration()) / (EarlyLoadConfig.SECONDS_UNTIL_BOOST.get() * 20), 1.0f);
        jumpRidingScale -= 0.1f;
        int displayWidth = Math.min((int) ((jumpRidingScale * 183.0F - Mth.randomBetween(ghast.getRandom(), 0.25f, 0.75f))), 183);

        guiGraphics.blit(HappyGhastsBoost.identifier("textures/gui/icons_copy.png"), xStart, yStart, (float) 0, (float) 84, 182, 5, 256, 256);
        if (displayWidth > 0) {
          guiGraphics.blit(HappyGhastsBoost.identifier("textures/gui/icons_copy.png"), xStart, yStart, (float) 0, (float) 89, displayWidth, 5, 256, 256);
        }
      }
    }
  }
}
