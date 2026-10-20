package io.github.jason13official.happy_ghasts_boost.mixin;

import com.blackgear.vanillabackport.common.level.entities.mob.animal.happy_ghast.HappyGhast;
import io.github.jason13official.happy_ghasts_boost.api.common.IHappyGhastBoostDataHolder;
import io.github.jason13official.happy_ghasts_boost.impl.common.EarlyLoadConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = HappyGhast.class, remap = false)
public class HappyGhastMixin implements IHappyGhastBoostDataHolder {

  @Unique
  private boolean happy_ghasts_boost$boosted = false;

  @Unique
  private int happy_ghasts_boost$forwardFlightDuration = 0;

  @Unique
  private float happy_ghasts_boost$oldYDegrees = 0.0f;

  @Override
  public void happy_ghasts_boost$tick() {

    HappyGhast ghast = (HappyGhast) (Object) this;
    if (ghast.getControllingPassenger() == null) {
      happy_ghasts_boost$resetTrackedValues();
      return;
    } // the ghast has a passenger

    // actual
    Vec3 movement = ghast.getDeltaMovement();
    Vec3 forward = ghast.getForward();

    boolean movingForward = happy_ghasts_boost$isMovingForward(movement, forward);
    boolean velocityCheck = happy_ghasts_boost$checkThreshold(movement.x, movement.z);

    if (movingForward && velocityCheck) {

      float currentYDegrees = ghast.getVisualRotationYInDegrees();
      boolean turnedTooFar = Mth.degreesDifferenceAbs(currentYDegrees, happy_ghasts_boost$oldYDegrees) > 5.0F;

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

    if (happy_ghasts_boost$boosted && ghast.level().isClientSide()) {
      Vec3 pos = ghast.position();
      ghast.level().addParticle(ParticleTypes.POOF, pos.x, pos.y, pos.z, 0.1, 0.1, 0.1);
    }
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
  private static boolean happy_ghasts_boost$isMovingForward(Vec3 movement, Vec3 forward) {

    Vec3 horizontalMovement = new Vec3(movement.x, 0.0D, movement.z);
    Vec3 horizontalForward = new Vec3(forward.x, 0.0D, forward.z);

    if (horizontalMovement.lengthSqr() < 1.0E-7D || horizontalForward.lengthSqr() < 1.0E-7D) {
      return false;
    }

    return horizontalMovement.normalize().dot(horizontalForward.normalize()) >= 0.99D;
  }

  @Unique
  private static boolean happy_ghasts_boost$checkThreshold(double xDelta, double zDelta) {

    float checkedValue = Mth.sqrt((float) Mth.square(xDelta)) + Mth.sqrt((float) Mth.square(zDelta));

    return checkedValue > 0.1;
  }
}
