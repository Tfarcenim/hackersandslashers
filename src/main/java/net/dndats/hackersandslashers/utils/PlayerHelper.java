package net.dndats.hackersandslashers.utils;

import java.util.Map;
import java.util.Objects;
import net.dndats.hackersandslashers.common.data.IsBashingData;
import net.dndats.hackersandslashers.common.data.IsParryingData;
import net.dndats.hackersandslashers.common.data.VisibilityLevelData;
import net.dndats.hackersandslashers.common.setup.ModAttributes;
import net.dndats.hackersandslashers.common.setup.ModData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.neoforge.registries.DeferredHolder;

public class PlayerHelper {
   private static final int DARK_PLACE_THRESHOLD = 8;
   private static final double HEAVY_ATTACK_SPEED = 1.4D;
   private static final double LIGHT_ATTACK_SPEED = 1.9D;
   private static final Map<String, AttributeModifier> PARRY_MODIFIERS;

   public static boolean isAtDarkPlace(Player player) {
      return lightLevel(player) < 8;
   }

   public static int lightLevel(Player player) {
      Level level = player.level();
      BlockPos position = player.blockPosition();
      return Math.max(level.getBrightness(LightLayer.SKY, position), level.getBrightness(LightLayer.BLOCK, position));
   }

   public static double getSneakiness(Player player) {
      return getAttributeValue(player, ModAttributes.SNEAKINESS);
   }

   public static float getSneakyAttackFactorAugment(Player player) {
      return (float)getAttributeValue(player, ModAttributes.SNEAKY_ATTACK_POWER);
   }

   public static float getRiposteFactorAugment(Player player) {
      return (float)getAttributeValue(player, ModAttributes.RIPOSTE_POWER);
   }

   public static float getHeadshotFactorAugment(Player player) {
      return (float)getAttributeValue(player, ModAttributes.HEADSHOT_POWER);
   }

   public static int getArmorLevel(Player player) {
      return player.getArmorValue();
   }

   public static boolean isPlayerBehind(LivingEntity target, Player player) {
      Vec3 mobForward = Vec3.directionFromRotation(0.0F, target.getYRot());
      Vec3 mobToPlayer = (new Vec3(player.getX() - target.getX(), player.getY() - target.getY(), player.getZ() - target.getZ())).normalize();
      double angle = Math.acos(mobForward.dot(mobToPlayer)) * 57.29577951308232D;
      return angle > 90.0D;
   }

   public static boolean isOnBush(Player player) {
      Block block = player.level().getBlockState(player.blockPosition()).getBlock();
      return block instanceof BushBlock;
   }

   public static boolean isMoving(Player player) {
      return player.getSpeed() > 0.0F;
   }

   public static boolean isParrying(Player player) {
      return ((IsParryingData)player.getData(ModData.IS_PARRYING)).getIsParrying();
   }

   public static boolean isBashing(Player player) {
      return ((IsBashingData)player.getData(ModData.IS_BASHING)).isBashing();
   }

   public static int getVisibilityLevel(Player player) {
      return player == null ? 0 : ((VisibilityLevelData)player.getData(ModData.VISIBILITY_LEVEL)).getVisibilityLevel();
   }

   public static void updateVisibilityLevel(Player player, int newVisibilityLevel) {
      if (getVisibilityLevel(player) != newVisibilityLevel) {
         ((VisibilityLevelData)player.getData(ModData.VISIBILITY_LEVEL)).setVisibilityLevel(newVisibilityLevel);
         ((VisibilityLevelData)player.getData(ModData.VISIBILITY_LEVEL)).syncData(player);
      }

   }

   public static boolean isPointingAtInteractable(Player player) {
      HitResult hit = player.pick(5.0D, 0.0F, false);
      if (hit.getType() != Type.BLOCK) {
         return false;
      } else {
         BlockHitResult blockHitResult = (BlockHitResult)hit;
         BlockPos blockPos = blockHitResult.getBlockPos();
         Level world = player.level();
         BlockState blockState = world.getBlockState(blockPos);
         BlockEntity blockEntity = world.getBlockEntity(blockPos);
         return blockEntity != null || blockState.getBlock() instanceof DoorBlock || blockState.getBlock() instanceof TrapDoorBlock;
      }
   }

   public static void addSpeedModifier(Player player, ItemStack item) {
      if (!player.level().isClientSide) {
         double attackSpeed = (double)ItemHelper.getAttackSpeed(item, player);
         if (attackSpeed <= 1.4D) {
            applyModifier(player, (AttributeModifier)PARRY_MODIFIERS.get("heavy"));
         } else if (attackSpeed >= 1.9D) {
            applyModifier(player, (AttributeModifier)PARRY_MODIFIERS.get("light"));
         } else {
            applyModifier(player, (AttributeModifier)PARRY_MODIFIERS.get("generic"));
         }
      }

   }

   public static void removeSpeedModifier(Player player) {
      if (!player.level().isClientSide) {
         PARRY_MODIFIERS.values().forEach((modifier) -> {
            removeModifier(player, modifier.id());
         });
      }

   }

   private static void applyModifier(Player player, AttributeModifier modifier) {
      AttributeInstance attribute = (AttributeInstance)Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED));
      if (!attribute.hasModifier(modifier.id())) {
         attribute.addTransientModifier(modifier);
      }

   }

   private static void removeModifier(Player player, ResourceLocation location) {
      AttributeInstance attribute = (AttributeInstance)Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED));
      if (attribute.hasModifier(location)) {
         attribute.removeModifier(location);
      }

   }

   private static double getAttributeValue(Player player, DeferredHolder<Attribute, Attribute> attribute) {
      return ((AttributeInstance)Objects.requireNonNull(player.getAttribute(attribute))).getValue();
   }

   static {
      PARRY_MODIFIERS = Map.of("heavy", new AttributeModifier(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "parry_heavy_modifier"), -0.045D, Operation.ADD_VALUE), "generic", new AttributeModifier(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "parry_generic_modifier"), -0.045D, Operation.ADD_VALUE), "light", new AttributeModifier(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "parry_light_modifier"), -0.045D, Operation.ADD_VALUE));
   }
}
