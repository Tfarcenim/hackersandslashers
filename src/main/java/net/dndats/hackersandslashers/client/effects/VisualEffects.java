package net.dndats.hackersandslashers.client.effects;

import net.dndats.hackersandslashers.common.setup.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.Level;

public class VisualEffects {
   public static void spawnCriticalParticle(Level level, double x, double y, double z, DamageSource damageSource) {
      ServerLevel serverLevel;
      if (!damageSource.is(DamageTypes.GENERIC) && !damageSource.is(DamageTypes.PLAYER_ATTACK) && !damageSource.is(DamageTypes.ARROW)) {
         if (damageSource.getClass().getSimpleName().contains("Spell")) {
            if (level instanceof ServerLevel) {
               serverLevel = (ServerLevel)level;
               serverLevel.sendParticles((SimpleParticleType)ModParticles.CRIT_MAGIC.get(), x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            } else {
               level.addParticle((ParticleOptions)ModParticles.CRIT_MAGIC.get(), x, y, z, 0.0D, 0.0D, 0.0D);
            }
         }
      } else if (level instanceof ServerLevel) {
         serverLevel = (ServerLevel)level;
         serverLevel.sendParticles((SimpleParticleType)ModParticles.CRIT_GENERIC.get(), x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
      } else {
         level.addParticle((ParticleOptions)ModParticles.CRIT_GENERIC.get(), x, y, z, 0.0D, 0.0D, 0.0D);
      }

   }

   public static void spawnAttackParticles(Level level, double x, double y, double z, int damageAmount) {
      if (damageAmount > 20) {
         damageAmount = 20;
      }

      if (damageAmount < 5) {
         damageAmount = 5;
      }

      if (level instanceof ServerLevel) {
         ServerLevel serverLevel = (ServerLevel)level;
         serverLevel.sendParticles((SimpleParticleType)ModParticles.ATTACK_SPARK.get(), x, y, z, damageAmount, 0.0D, 0.0D, 0.0D, 1.0D);
      }

      for(int i = 0; i < 5; ++i) {
         level.addParticle((ParticleOptions)ModParticles.ATTACK_SPARK.get(), x, y, z, (level.random.nextDouble() - 0.5D) * 0.5D, (level.random.nextDouble() - 0.5D) * 0.5D, (level.random.nextDouble() - 0.5D) * 0.5D);
      }

   }

   public static void spawnBashParticles(Level level, double x, double y, double z) {
      if (level instanceof ServerLevel) {
         ServerLevel serverLevel = (ServerLevel)level;
         serverLevel.sendParticles(ParticleTypes.POOF, x, y, z, 5, 0.0D, 0.0D, 0.0D, 0.0D);
      }

      for(int i = 0; i < 5; ++i) {
         level.addParticle(ParticleTypes.POOF, x, y, z, 0.0D, 0.0D, 0.0D);
      }

   }

   public static void spawnAttackCritParticles(Level level, double x, double y, double z, int damageAmount) {
      if (damageAmount > 20) {
         damageAmount = 20;
      }

      if (damageAmount < 10) {
         damageAmount = 10;
      }

      if (level instanceof ServerLevel) {
         ServerLevel serverLevel = (ServerLevel)level;
         serverLevel.sendParticles((SimpleParticleType)ModParticles.ATTACK_SPARK_CRIT_GENERIC.get(), x, y, z, damageAmount, 0.0D, 0.0D, 0.0D, 1.0D);
      }

      for(int i = 0; i < 5; ++i) {
         level.addParticle((ParticleOptions)ModParticles.ATTACK_SPARK_CRIT_GENERIC.get(), x, y, z, (level.random.nextDouble() - 0.5D) * 0.5D, (level.random.nextDouble() - 0.5D) * 0.5D, (level.random.nextDouble() - 0.5D) * 0.5D);
      }

   }
}
