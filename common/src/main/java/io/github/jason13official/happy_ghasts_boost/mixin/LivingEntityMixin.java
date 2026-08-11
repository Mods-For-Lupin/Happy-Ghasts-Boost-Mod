package io.github.jason13official.happy_ghasts_boost.mixin;

import io.github.jason13official.happy_ghasts_boost.api.common.IHappyGhastBoostDataHolder;
import io.github.jason13official.happy_ghasts_boost.impl.common.EarlyLoadConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

  @Inject(at = @At("TAIL"), method = "getAttributeValue", cancellable = true)
  private void happy_ghasts_boost$getAttributeValue(Holder<Attribute> attribute, CallbackInfoReturnable<Double> cir) {

    LivingEntity living = (LivingEntity) (Object) this;

    if (!(living instanceof HappyGhast ghast) || !attribute.equals(Attributes.FLYING_SPEED)) {
      return;
    }

    IHappyGhastBoostDataHolder dataHolder = (IHappyGhastBoostDataHolder) ghast;
    MobEffectInstance effect = ghast.getEffect(MobEffects.SPEED);
    boolean boosted = dataHolder.happy_ghasts_boost$isBoosted();

    if (dataHolder.happy_ghasts_boost$getForwardFlightDuration() > 0 || effect != null) {

      if (boosted) {
        Vec3 pos = ghast.position();
        ghast.level().addParticle(ParticleTypes.POOF, pos.x, pos.y, pos.z, 0.1, 0.1, 0.1);
      }

      if (effect == null) {
        double percentage = Math.min(1.0D, dataHolder.happy_ghasts_boost$getForwardFlightDuration() / ((double) EarlyLoadConfig.SECONDS_UNTIL_BOOST.get() * 20.0D));
        double calculated = (0.05D * EarlyLoadConfig.FORWARD_MULTIPLIER.get()) * percentage;
        cir.setReturnValue(Math.max(0.05D, calculated));
      } else {

        float potionForwardMod = boosted ? 0.025f : 0f;

        if (effect.getAmplifier() == 0) {
          cir.setReturnValue((0.05 * (EarlyLoadConfig.POTION_MULTIPLIER.get())) + potionForwardMod);
        } else if (effect.getAmplifier() == 1) {
          cir.setReturnValue((0.05 * (EarlyLoadConfig.POTION_MULTIPLIER_TWO.get())) + potionForwardMod);
        }
      }
    }
  }
}