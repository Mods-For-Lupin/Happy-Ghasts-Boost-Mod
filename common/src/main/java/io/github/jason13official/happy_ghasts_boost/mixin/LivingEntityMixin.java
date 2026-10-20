package io.github.jason13official.happy_ghasts_boost.mixin;

import com.blackgear.vanillabackport.common.level.entities.mob.animal.happy_ghast.HappyGhast;
import io.github.jason13official.happy_ghasts_boost.api.common.IHappyGhastBoostDataHolder;
import io.github.jason13official.happy_ghasts_boost.impl.common.EarlyLoadConfig;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

  @Inject(at = @At("HEAD"), method = "tick")
  private void happy_ghasts_boost$tick(CallbackInfo ci) {

    if ((Object) this instanceof IHappyGhastBoostDataHolder dataHolder) {
      dataHolder.happy_ghasts_boost$tick();
    }
  }

  @Inject(at = @At("TAIL"), method = "getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D", cancellable = true)
  private void happy_ghasts_boost$getAttributeValue(Attribute attribute, CallbackInfoReturnable<Double> cir) {

    LivingEntity living = (LivingEntity) (Object) this;

    if (!(living instanceof HappyGhast ghast) || attribute != Attributes.FLYING_SPEED) {
      return;
    }

    IHappyGhastBoostDataHolder dataHolder = (IHappyGhastBoostDataHolder) ghast;
    MobEffectInstance effect = ghast.getEffect(MobEffects.MOVEMENT_SPEED);
    boolean boosted = dataHolder.happy_ghasts_boost$isBoosted();

    if (dataHolder.happy_ghasts_boost$getForwardFlightDuration() > 0 || effect != null) {

      if (effect == null) {
        double percentage = Math.min(1.0D, dataHolder.happy_ghasts_boost$getForwardFlightDuration() / ((double) EarlyLoadConfig.SECONDS_UNTIL_BOOST.get() * 20.0D));
        double calculated = (0.05D * EarlyLoadConfig.FORWARD_MULTIPLIER.get()) * percentage;
        cir.setReturnValue(Math.max(0.05D, calculated));
      } else {

        float potionForwardMod = boosted ? 0.025f : 0f;

        if (effect.getAmplifier() == 0) {
          cir.setReturnValue((0.05 * (EarlyLoadConfig.POTION_MULTIPLIER.get())) + potionForwardMod);
        } else {
          cir.setReturnValue((0.05 * (EarlyLoadConfig.POTION_MULTIPLIER_TWO.get())) + potionForwardMod);
        }
      }
    }
  }
}
