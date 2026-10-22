package io.github.jason13official.happy_ghasts_boost.mixin;

import com.blackgear.vanillabackport.common.level.entities.mob.animal.happy_ghast.HappyGhast;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownPotion.class)
public class ThrownPotionMixin {

  @Inject(method = "applySplash", at = @At("TAIL"))
  private void happy_ghasts_boost$applySplash(Iterable<MobEffectInstance> effects, Entity target, CallbackInfo ci) {

    ThrownPotion potion = (ThrownPotion) (Object) this;
    AABB area = potion.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);

    for (HappyGhast ghast : potion.level().getEntitiesOfClass(HappyGhast.class, area)) {

      boolean handledByVanilla = potion.distanceToSqr(ghast) < 16.0D;
      if (handledByVanilla || !ghast.isAffectedByPotions()) {
        continue;
      }

      double distance = happy_ghasts_boost$distanceToBoxSqr(potion.position(), ghast.getBoundingBox());
      if (distance >= 16.0D) {
        continue;
      }

      double scale = ghast == target ? 1.0D : 1.0D - Math.sqrt(distance) / 4.0D;

      for (MobEffectInstance instance : effects) {
        Holder<MobEffect> effect = instance.getEffect();
        if (effect.value().isInstantenous()) {
          effect.value().applyInstantenousEffect(potion, potion.getOwner(), ghast, instance.getAmplifier(), scale);
        } else {
          int duration = instance.mapDuration(ticks -> (int) (scale * ticks + 0.5D));
          MobEffectInstance scaled = new MobEffectInstance(effect, duration, instance.getAmplifier(), instance.isAmbient(), instance.isVisible());
          if (!scaled.endsWithin(20)) {
            ghast.addEffect(scaled, potion.getEffectSource());
          }
        }
      }
    }
  }

  @Unique
  private static double happy_ghasts_boost$distanceToBoxSqr(Vec3 pos, AABB box) {

    double x = Mth.clamp(pos.x, box.minX, box.maxX);
    double y = Mth.clamp(pos.y, box.minY, box.maxY);
    double z = Mth.clamp(pos.z, box.minZ, box.maxZ);

    return pos.distanceToSqr(x, y, z);
  }
}
