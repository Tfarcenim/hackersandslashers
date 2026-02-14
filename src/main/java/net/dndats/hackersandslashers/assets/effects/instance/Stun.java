package net.dndats.hackersandslashers.assets.effects.instance;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class Stun extends MobEffect {
   public Stun() {
      super(MobEffectCategory.HARMFUL, -1);
   }

   public void onEffectStarted(LivingEntity entity, int amplifier) {
      super.onEffectStarted(entity, amplifier);
      if (entity instanceof Mob) {
         Mob mob = (Mob)entity;
         mob.setNoAi(true);
      }

   }

   public boolean applyEffectTick(LivingEntity entity, int amplifier) {
      if (entity instanceof Player) {
         Player player = (Player)entity;
         player.setDeltaMovement(0.0D, 0.0D, 0.0D);
      }

      return true;
   }

   public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
      return true;
   }
}
