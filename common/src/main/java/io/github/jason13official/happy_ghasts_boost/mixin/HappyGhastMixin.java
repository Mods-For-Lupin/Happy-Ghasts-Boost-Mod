package io.github.jason13official.happy_ghasts_boost.mixin;

import io.github.jason13official.happy_ghasts_boost.api.common.IHappyGhastBoostDataHolder;
import io.github.jason13official.happy_ghasts_boost.impl.common.EarlyLoadConfig;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HappyGhast.class)
public class HappyGhastMixin implements IHappyGhastBoostDataHolder {

  @Unique
  private boolean happy_ghasts_boost$boosted = false;

  @Unique
  private int happy_ghasts_boost$forwardFlightDuration = 0;

  @Unique
  private float happy_ghasts_boost$oldYDegrees = 0.0f;

  @Inject(at = @At("HEAD"), method = "tick")
  private void faster_happy_ghasts$tick(CallbackInfo ci) {

    HappyGhast ghast = (HappyGhast) (Object) this;
    if (ghast.getControllingPassenger() == null) {
      return;
    } // the ghast has a passenger

    // actual
    Vec3 movement = ghast.getDeltaMovement();
    Vec3 forward = ghast.getForward();

    // normalized
    Vec3 movementNormal = movement.normalize();
    Vec3 forwardNormal = forward.normalize();

    boolean movingForward = happy_ghasts_boost$withinFivePercentMargin(movementNormal.x, forwardNormal.x) && happy_ghasts_boost$withinFivePercentMargin(movementNormal.z, forwardNormal.z);
    boolean velocityCheck = happy_ghasts_boost$checkThreshold(movement.x, movement.z);

    if (movingForward && velocityCheck) {

      float currentYDegrees = ghast.getVisualRotationYInDegrees();
      boolean turnedTooFar = !happy_ghasts_boost$withinFivePercentMargin(currentYDegrees, happy_ghasts_boost$oldYDegrees);

      if (turnedTooFar) {
        happy_ghasts_boost$resetTrackedValues();
      }

      boolean readyToBoost = happy_ghasts_boost$forwardFlightDuration / 20 >= EarlyLoadConfig.SECONDS_UNTIL_BOOST.get();
      if (readyToBoost) {
        happy_ghasts_boost$boosted = true;
      }

      // increment forward-flight duration
      happy_ghasts_boost$forwardFlightDuration += 1;

    } else if (!movingForward || !velocityCheck) {
      happy_ghasts_boost$resetTrackedValues(); // reset boost on no movement/oversteering
    }

    // track for oversteering
    happy_ghasts_boost$oldYDegrees = ghast.getVisualRotationYInDegrees();
  }

  @Override
  public boolean happy_ghasts_boost$isBoosted() {
    return this.happy_ghasts_boost$boosted;
  }

  @Override
  public int happy_ghasts_boost$getForwardFlightDuration() {
    return this.happy_ghasts_boost$forwardFlightDuration;
  }

  @Unique
  private void happy_ghasts_boost$resetTrackedValues() {
    this.happy_ghasts_boost$forwardFlightDuration = 0;
    this.happy_ghasts_boost$boosted = false;
  }

  @Unique
  private static boolean happy_ghasts_boost$withinFivePercentMargin(double a, double b) {

    double c = Math.abs(a - b) / a;

    return c <= 0.05D; // 5% threshold
  }

  @Unique
  private static boolean happy_ghasts_boost$checkThreshold(double xDelta, double zDelta) {

    float checkedValue = Mth.sqrt((float) Mth.square(xDelta)) + Mth.sqrt((float) Mth.square(zDelta));

    return checkedValue > 0.1;
  }
}
