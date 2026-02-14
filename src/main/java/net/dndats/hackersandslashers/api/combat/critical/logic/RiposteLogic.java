package net.dndats.hackersandslashers.api.combat.critical.logic;

import net.dndats.hackersandslashers.api.interfaces.ICriticalLogic;
import net.dndats.hackersandslashers.api.item.WeaponCategory;
import net.dndats.hackersandslashers.api.item.WeaponCategoryVerifier;
import net.dndats.hackersandslashers.common.setup.ModMobEffects;
import net.dndats.hackersandslashers.utils.ItemHelper;
import net.dndats.hackersandslashers.utils.PlayerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class RiposteLogic implements ICriticalLogic {
   private final float DAMAGE_MULTIPLIER;

   public RiposteLogic(float damageMultiplier) {
      if (damageMultiplier <= 0.0F) {
         throw new IllegalArgumentException("Damage multiplier must be greater than 0");
      } else {
         this.DAMAGE_MULTIPLIER = damageMultiplier;
      }
   }

   public boolean canBeApplied(Entity source, LivingEntity target) {
      return !(source instanceof Player) ? false : target.hasEffect(ModMobEffects.STUN);
   }

   public float getAdditionalModifiers(LivingIncomingDamageEvent event) {
      Entity var3 = event.getSource().getEntity();
      if (var3 instanceof Player) {
         Player player = (Player)var3;
         ItemStack usedItem = event.getSource().getWeaponItem();
         WeaponCategory usedWeaponCategory = WeaponCategoryVerifier.getUsingWeaponCategory(player);
         if (usedItem == null) {
            return 0.0F;
         } else if (usedWeaponCategory != WeaponCategory.MEDIUM && usedWeaponCategory != WeaponCategory.HEAVY) {
            return PlayerHelper.getRiposteFactorAugment(player) / 100.0F;
         } else {
            float attackSpeed = ItemHelper.getAttackSpeed(usedItem, player);
            float attackDamage = ItemHelper.getAttackDamage(usedItem, player);
            float multiplier;
            if (usedWeaponCategory == WeaponCategory.MEDIUM) {
               multiplier = 0.5F;
            } else {
               multiplier = 0.25F;
            }

            float damageMultiplier = (attackSpeed + attackDamage) * multiplier * PlayerHelper.getRiposteFactorAugment(player);
            return (float)Math.max(Math.round(damageMultiplier), 0) * (PlayerHelper.getRiposteFactorAugment(player) / 100.0F + 1.0F);
         }
      } else {
         return 0.0F;
      }
   }

   public void applyOnHitFunction(LivingIncomingDamageEvent event) {
      if (event.getEntity().hasEffect(ModMobEffects.STUN)) {
         event.getEntity().removeEffect(ModMobEffects.STUN);
      }

   }

   public float getDamageMultiplier() {
      return this.DAMAGE_MULTIPLIER;
   }
}
