package net.dndats.hackersandslashers.utils;

import java.util.List;
import net.dndats.hackersandslashers.common.data.MobData;
import net.dndats.hackersandslashers.common.setup.ModData;
import net.dndats.hackersandslashers.common.setup.ModMobEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class EntityHelper {
   private static final String LOST_HEALTH_PERCENT = "lost_health_percent";
   private static final int STUN_DURATION_TICKS = 60;

   public static boolean isBeingTargeted(Player target, LivingEntity source) {
      boolean var10000;
      if (source instanceof Mob) {
         Mob mob = (Mob)source;
         if (mob.getTarget() != null && mob.getTarget().is(target)) {
            var10000 = true;
            return var10000;
         }
      }

      var10000 = false;
      return var10000;
   }

   public static List<Player> getMobMemories(Mob mob) {
      return getDetectabilityData(mob).getMobMemory();
   }

   public static void clearMobMemories(Mob mob) {
      getDetectabilityData(mob).clearMemory();
   }

   public static boolean hasMobMemories(Mob mob) {
      return getDetectabilityData(mob).hasMemories();
   }

   public static void makeMobRemember(Mob mob, Player player) {
      getDetectabilityData(mob).addOnMemory(player);
   }

   public static void makeMobForget(Mob mob, Player player) {
      getDetectabilityData(mob).forgetMemory(player);
   }

   public static boolean doMobRemember(Mob mob, Player player) {
      return getDetectabilityData(mob).hasOnMemory(player);
   }

   public static int getMobLostHealth(LivingEntity entity) {
      return entity.getPersistentData().getInt("lost_health_percent");
   }

   public static void updateLostHealth(LivingEntity entity) {
      CompoundTag nbt = entity.getPersistentData();
      float maxHealth = entity.getMaxHealth();
      float currentHealth = entity.getHealth();
      int lostHealthPercent = (int)((1.0F - currentHealth / maxHealth) * 100.0F);
      nbt.putInt("lost_health_percent", lostHealthPercent);
   }

   public static void setCurrentTargetId(Mob mob, Player player) {
      getDetectabilityData(mob).setCurrentTarget(player.getUUID().toString());
   }

   public static void resetCurrentTargetId(Mob mob) {
      getDetectabilityData(mob).setCurrentTarget("");
   }

   public static String getCurrentTargetId(Mob mob) {
      return getDetectabilityData(mob).getCurrentTarget();
   }

   public static void setAlert(Mob mob, boolean isAlert) {
      getDetectabilityData(mob).setAlert(isAlert);
   }

   public static boolean isAlert(Mob mob) {
      return getDetectabilityData(mob).isAlert();
   }

   public static void setAlertLevel(Mob mob, int alertLevel) {
      getDetectabilityData(mob).setAlertLevel(alertLevel);
   }

   public static int getAlertLevel(Mob mob) {
      return getDetectabilityData(mob).getAlertLevel();
   }

   public static int getDistanceBetweenEntities(LivingEntity entity1, LivingEntity entity2) {
      if (entity1 != null && entity2 != null) {
         double dx = entity2.getX() - entity1.getX();
         double dy = entity2.getY() - entity1.getY();
         double dz = entity2.getZ() - entity1.getZ();
         return (int)Math.sqrt(dx * dx + dy * dy + dz * dz);
      } else {
         return 0;
      }
   }

   public static boolean canSee(Player player, LivingEntity entity) {
      return entity.hasLineOfSight(player);
   }

   public static boolean isInCombatWithPlayer(Player player, LivingEntity entity) {
      return entity.getLastAttacker() == player;
   }

   public static void stunTarget(LivingEntity entity) {
      if (entity == null) {
         throw new IllegalArgumentException("Entity cannot be null.");
      } else {
         entity.addEffect(new MobEffectInstance(ModMobEffects.STUN, 60, 1, false, false));
      }
   }

   private static MobData getDetectabilityData(Mob mob) {
      return (MobData)mob.getData(ModData.MOB_DETECTABILITY);
   }
}
