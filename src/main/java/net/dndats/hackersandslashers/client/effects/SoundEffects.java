package net.dndats.hackersandslashers.client.effects;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SoundEffects {
   public static void playCriticalSound(LivingEntity entity) {
      entity.level().playSound((Player)null, entity.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0F, 1.0F);
   }

   public static void playParrySound(LivingEntity entity) {
      entity.level().playSound((Player)null, entity.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
   }

   public static void playBashSound(LivingEntity entity) {
      entity.level().playSound((Player)null, entity.blockPosition(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.PLAYERS, 1.0F, 1.0F);
   }

   public static void playBashSwingSound(LivingEntity entity) {
      entity.level().playSound((Player)null, entity.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F);
   }

   public static void playParrySwingSound(LivingEntity entity) {
      entity.level().playSound((Player)null, entity.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F);
   }
}
