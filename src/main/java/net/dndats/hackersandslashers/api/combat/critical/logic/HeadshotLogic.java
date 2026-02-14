package net.dndats.hackersandslashers.api.combat.critical.logic;

import net.dndats.hackersandslashers.api.interfaces.ICriticalLogic;
import net.dndats.hackersandslashers.utils.PlayerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class HeadshotLogic implements ICriticalLogic {
   private final float DAMAGE_MULTIPLIER;

   public HeadshotLogic(float damageMultiplier) {
      if (damageMultiplier <= 0.0F) {
         throw new IllegalArgumentException("Damage multiplier must be greater than 0");
      } else {
         this.DAMAGE_MULTIPLIER = damageMultiplier;
      }
   }

   public boolean canBeApplied(Entity directSourceEntity, LivingEntity target) {
      if (!(directSourceEntity instanceof Projectile)) {
         return false;
      } else {
         Projectile projectile = (Projectile)directSourceEntity;
         double targetEyeHeight = (double)target.getEyeHeight();
         double targetEyeYCenter = target.getY() + targetEyeHeight;
         double tolerance = 0.5D;
         return projectile.position().y >= targetEyeYCenter - tolerance && projectile.position().y <= targetEyeYCenter + tolerance;
      }
   }

   public float getAdditionalModifiers(LivingIncomingDamageEvent event) {
      Entity var3 = event.getSource().getEntity();
      if (var3 instanceof Player) {
         Player player = (Player)var3;
         return this.getDamageMultiplier() * PlayerHelper.getHeadshotFactorAugment(player);
      } else {
         return 0.0F;
      }
   }

   public void applyOnHitFunction(LivingIncomingDamageEvent event) {
   }

   public float getDamageMultiplier() {
      return this.DAMAGE_MULTIPLIER;
   }
}
