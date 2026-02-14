package net.dndats.hackersandslashers.api.combat.critical.logic;

import net.dndats.hackersandslashers.api.interfaces.ICriticalLogic;
import net.dndats.hackersandslashers.api.item.WeaponCategory;
import net.dndats.hackersandslashers.api.item.WeaponCategoryVerifier;
import net.dndats.hackersandslashers.utils.EntityHelper;
import net.dndats.hackersandslashers.utils.ItemHelper;
import net.dndats.hackersandslashers.utils.PlayerHelper;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class SneakyAttackLogic implements ICriticalLogic {
   private final float DAMAGE_MULTIPLIER;

   public SneakyAttackLogic(float damageMultiplier) {
      if (damageMultiplier <= 0.0F) {
         throw new IllegalArgumentException("Damage multiplier must be greater than 0");
      } else {
         this.DAMAGE_MULTIPLIER = damageMultiplier;
      }
   }

   public boolean canBeApplied(Entity source, LivingEntity target) {
      if (!(source instanceof Player)) {
         return false;
      } else {
         Player player = (Player)source;
         if (target instanceof Player) {
            return PlayerHelper.isPlayerBehind(target, player) || !EntityHelper.isInCombatWithPlayer(player, target);
         } else if (!(target instanceof Mob)) {
            return false;
         } else {
            Mob mob = (Mob)target;
            if (mob instanceof AgeableMob) {
               AgeableMob ageableMob = (AgeableMob)mob;
               return !ageableMob.isPanicking() && ageableMob.getLastAttacker() != player;
            } else {
               return PlayerHelper.isPlayerBehind(target, player) || mob.getTarget() != player;
            }
         }
      }
   }

   public float getAdditionalModifiers(LivingIncomingDamageEvent event) {
      Entity var3 = event.getSource().getEntity();
      if (var3 instanceof Player) {
         Player player = (Player)var3;
         ItemStack usedItem = event.getSource().getWeaponItem();
         WeaponCategory usedWeaponCategory = WeaponCategoryVerifier.getUsingWeaponCategory(player);
         if (usedItem == null) {
            return 0.0F;
         } else if (usedWeaponCategory == WeaponCategory.LIGHT) {
            float attackSpeed = ItemHelper.getAttackSpeed(usedItem, player);
            float baseDamage = ItemHelper.getAttackDamage(usedItem, player);
            float multiplier = 10.0F;
            float alpha = 1.9F;
            float cap = 20.0F;
            float speedMultiplier = (float)Math.pow((double)(attackSpeed - alpha), (double)multiplier);
            speedMultiplier = Math.min(speedMultiplier, cap);
            float damageMultiplier = (1.0F + speedMultiplier) * baseDamage;
            return (float)Math.max(Math.round(damageMultiplier), 0) * (PlayerHelper.getSneakyAttackFactorAugment(player) / 100.0F + 1.0F);
         } else {
            return PlayerHelper.getSneakyAttackFactorAugment(player) / 100.0F;
         }
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
