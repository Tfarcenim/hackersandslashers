package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.effects.VisualEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class CombatHelper {
   private static final double PARTICLE_Y_OFFSET = 0.25D;

   public static float dealCriticalDamage(float multiplier, LivingIncomingDamageEvent event) {
      if (multiplier <= 0.0F) {
         HackersAndSlashers.LOGGER.warn("Invalid critical damage multiplier: {}", multiplier);
         return 0.0F;
      } else {
         try {
            float finalAmount = event.getOriginalAmount() * multiplier;
            event.setAmount(finalAmount);
            return finalAmount;
         } catch (Exception var3) {
            HackersAndSlashers.LOGGER.error("Error while applying critical damage: {}", var3.getMessage());
            return 0.0F;
         }
      }
   }

   public static void stunAttackingEntity(LivingIncomingDamageEvent event) {
      Entity var3 = event.getSource().getEntity();
      LivingEntity var10000;
      if (var3 instanceof LivingEntity) {
         LivingEntity entity = (LivingEntity)var3;
         var10000 = entity;
      } else {
         var10000 = null;
      }

      LivingEntity attacker = var10000;
      if (attacker != null && event.getEntity() instanceof Player) {
         try {
            EntityHelper.stunTarget(attacker);
         } catch (Exception var4) {
            HackersAndSlashers.LOGGER.error("Error while stunning target: {}", var4.getMessage());
         }

      }
   }

   public static void spawnCombatParticles(LivingIncomingDamageEvent event, boolean isCritical) {
      LivingEntity target = event.getEntity();
      if (target == null) {
         HackersAndSlashers.LOGGER.warn("Target entity is null for particle spawning.");
      } else {
         ItemStack weaponItemStack = event.getSource().getWeaponItem();
         Item usedItem = weaponItemStack != null ? weaponItemStack.getItem() : null;
         if (usedItem instanceof SwordItem) {
            spawnParticles(target.level(), target.getX(), target.getY() + 0.25D + (double)(target.getBbHeight() / 2.0F), target.getZ(), (int)event.getAmount(), isCritical);
         } else {
            Entity var6 = event.getSource().getDirectEntity();
            if (var6 instanceof Projectile) {
               Projectile projectile = (Projectile)var6;
               spawnParticles(projectile.level(), projectile.getX(), projectile.getY(), projectile.getZ(), (int)event.getAmount(), isCritical);
            }
         }

      }
   }

   private static void spawnParticles(Level level, double x, double y, double z, int amount, boolean isCritical) {
      if (isCritical) {
         VisualEffects.spawnAttackCritParticles(level, x, y, z, amount);
      } else {
         VisualEffects.spawnAttackParticles(level, x, y, z, amount);
      }

   }
}
