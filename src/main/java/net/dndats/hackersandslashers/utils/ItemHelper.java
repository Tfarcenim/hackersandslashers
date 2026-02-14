package net.dndats.hackersandslashers.utils;

import java.util.Objects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.LevelAccessor;

public class ItemHelper {
   private static final float BASE_ATTACK_SPEED_ADJUSTMENT = 4.0F;

   public static void damageBlockWeapon(LivingEntity livingEntity, int amount) {
      if (livingEntity != null) {
         ItemStack mainHand = livingEntity.getMainHandItem();
         ItemStack offHand = livingEntity.getOffhandItem();
         if (mainHand.getItem() instanceof SwordItem && offHand.getItem() instanceof SwordItem) {
            damageAndDistribute(livingEntity.level(), mainHand, offHand, amount);
         } else {
            damage(livingEntity.level(), mainHand, amount);
         }

      }
   }

   private static void damage(LevelAccessor world, ItemStack item, int amount) {
      if (!isNullOrInvalid(world, item)) {
         if (world instanceof ServerLevel) {
            ServerLevel level = (ServerLevel)world;
            item.hurtAndBreak(amount, level, (ServerPlayer)null, (stackProvider) -> {
            });
         }

      }
   }

   private static void damageAndDistribute(LevelAccessor world, ItemStack item1, ItemStack item2, int amount) {
      if (!isNullOrInvalid(world, item1) && !isNullOrInvalid(world, item2)) {
         if (world instanceof ServerLevel) {
            ServerLevel level = (ServerLevel)world;
            int halfAmount = amount / 2;
            item1.hurtAndBreak(halfAmount, level, (ServerPlayer)null, (stackProvider) -> {
            });
            item2.hurtAndBreak(halfAmount, level, (ServerPlayer)null, (stackProvider) -> {
            });
         }

      }
   }

   private static boolean isNullOrInvalid(LevelAccessor world, ItemStack item) {
      return world == null || item == null || item.isEmpty();
   }

   public static boolean isHoldingParryItem(Player player) {
      if (player == null) {
         return false;
      } else {
         ItemStack item = player.getWeaponItem();
         return item.getAttributeModifiers().modifiers().stream().anyMatch((entry) -> {
            return entry.attribute() == Attributes.ATTACK_SPEED || entry.attribute() == Attributes.ATTACK_DAMAGE;
         }) && item.getUseDuration(player) == 0 && item.getUseAnimation() != UseAnim.SPEAR;
      }
   }

   public static boolean isHoldingBashItem(Player player) {
      if (player == null) {
         return false;
      } else {
         return player.getMainHandItem().getItem() instanceof ShieldItem || player.getOffhandItem().getItem() instanceof ShieldItem;
      }
   }

   public static float getAttackSpeed(ItemStack item, Player player) {
      return item != null && player != null ? (Float)item.getAttributeModifiers().modifiers().stream().filter((entry) -> {
         return entry.attribute() == Attributes.ATTACK_SPEED;
      }).map((entry) -> {
         double baseAttackSpeed = ((AttributeInstance)Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_SPEED))).getBaseValue();
         double additionalAttackSpeed = entry.modifier().amount();
         return (float)(baseAttackSpeed + additionalAttackSpeed);
      }).findFirst().orElse(0.0F) : 0.0F;
   }

   public static float getBaseAttackSpeed(ItemStack item) {
      return item == null ? 0.0F : (Float)item.getAttributeModifiers().modifiers().stream().filter((entry) -> {
         return entry.attribute() == Attributes.ATTACK_SPEED;
      }).map((entry) -> {
         return (float)(entry.modifier().amount() + 4.0D);
      }).findFirst().orElse(0.0F);
   }

   public static float getAttackDamage(ItemStack item, Player player) {
      return item != null && player != null ? (Float)item.getAttributeModifiers().modifiers().stream().filter((entry) -> {
         return entry.attribute() == Attributes.ATTACK_DAMAGE;
      }).map((entry) -> {
         return (float)entry.modifier().amount();
      }).findFirst().orElse(0.0F) : 0.0F;
   }

   public static String getRegistryName(ItemStack itemStack) {
      return itemStack != null && !itemStack.isEmpty() ? itemStack.getItem().toString().toLowerCase() : "unknown_item";
   }
}
