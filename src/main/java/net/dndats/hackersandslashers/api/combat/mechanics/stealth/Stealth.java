package net.dndats.hackersandslashers.api.combat.mechanics.stealth;

import java.util.Objects;
import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.common.data.MobData;
import net.dndats.hackersandslashers.common.setup.ModData;
import net.dndats.hackersandslashers.utils.EntityHelper;
import net.dndats.hackersandslashers.utils.PlayerHelper;
import net.dndats.hackersandslashers.utils.StealthHelper;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class Stealth {
   private static final int IS_INVISIBLE_WEIGHT;
   private static final int IS_BEHIND_WEIGHT;
   private static final int IS_AT_DARK_PLACE;
   private static final int SPRINTING_WEIGHT;
   private static final int ON_BUSH_WEIGHT;
   private static final int LAST_ATTACKER_WEIGHT;
   private static final int BEING_SEEN_WEIGHT;
   private static final int CROUCHING_WEIGHT;
   private static final int RAINING_WEIGHT;
   private static final int MOVING_WEIGHT;

   public static void stealthBehavior(LivingChangeTargetEvent event) {
      LivingEntity var3 = event.getEntity();
      if (var3 instanceof Mob) {
         Mob mob = (Mob)var3;
         var3 = event.getNewAboutToBeSetTarget();
         if (var3 instanceof Player) {
            Player player = (Player)var3;
            int alertPoints = calculateAlertPoints(player, mob);
            int alertLevel = EntityHelper.getAlertLevel(mob);
            EntityHelper.setCurrentTargetId(mob, player);
            if (alertLevel == 0 && !EntityHelper.isInCombatWithPlayer(player, mob)) {
               mob.setTarget((LivingEntity)null);
            }

            if (EntityHelper.canSee(player, mob)) {
               if (alertLevel < 100) {
                  int newAlertLevel = Math.min(100, Math.max(0, alertLevel + alertPoints));
                  EntityHelper.setAlertLevel(mob, newAlertLevel);
                  EntityHelper.setAlert(mob, true);
               }
            } else if (PlayerHelper.isOnBush(player) || PlayerHelper.isAtDarkPlace(player) || !EntityHelper.canSee(player, mob)) {
               if (EntityHelper.getAlertLevel(mob) == 0) {
                  EntityHelper.resetCurrentTargetId(mob);
               }

               EntityHelper.setAlert(mob, false);
            }

            if (alertLevel < 100) {
               event.setCanceled(true);
            }
         }
      }

   }

   private static int calculateAlertPoints(Player player, Mob mob) {
      int points = 0;
      int points = points - EntityHelper.getDistanceBetweenEntities(player, mob);
      points -= (int)PlayerHelper.getSneakiness(player);
      points += PlayerHelper.getArmorLevel(player) + PlayerHelper.lightLevel(player);
      points -= PlayerHelper.isAtDarkPlace(player) ? IS_AT_DARK_PLACE : 0;
      points -= !player.level().isRaining() && !player.level().isThundering() ? 0 : RAINING_WEIGHT;
      points += player.isSprinting() ? SPRINTING_WEIGHT : 0;
      points -= player.isCrouching() ? CROUCHING_WEIGHT : 0;
      points -= player.isInvisible() ? IS_INVISIBLE_WEIGHT : 0;
      points -= PlayerHelper.isOnBush(player) ? ON_BUSH_WEIGHT : 0;
      points += PlayerHelper.isMoving(player) ? MOVING_WEIGHT : 0;
      points -= PlayerHelper.isPlayerBehind(mob, player) ? IS_BEHIND_WEIGHT : 0;
      points += EntityHelper.isInCombatWithPlayer(player, mob) ? LAST_ATTACKER_WEIGHT : 0;
      points += StealthHelper.isBeingSeen(player) ? BEING_SEEN_WEIGHT : 0;
      return points;
   }

   public static void updatePlayerVisibility(Player player) {
      if (player != null) {
         StealthHelper.mobAlertSetter(player);
         Vec3 surroundings = new Vec3(player.getX(), player.getY(), player.getZ());
         Mob mostAlertMob = (Mob)player.level().getEntitiesOfClass(Mob.class, (new AABB(surroundings, surroundings)).inflate(32.0D)).stream().filter((mob) -> {
            MobData data = (MobData)mob.getData(ModData.MOB_DETECTABILITY);
            if (data.getAlertLevel() > 0) {
               if (mob instanceof AgeableMob) {
                  return mob.getLastAttacker() == player;
               } else {
                  return !Objects.equals(data.getCurrentTarget(), "") && data.getCurrentTarget().equals(player.getUUID().toString());
               }
            } else {
               return false;
            }
         }).max((mob1, mob2) -> {
            int alertMob1 = EntityHelper.getAlertLevel(mob1);
            int alertMob2 = EntityHelper.getAlertLevel(mob2);
            return Integer.compare(alertMob1, alertMob2);
         }).orElse((Object)null);
         if (mostAlertMob != null) {
            int alertLevel = EntityHelper.getAlertLevel(mostAlertMob);
            int newVisibilityLevel = calculateVisibilityLevel(alertLevel);
            PlayerHelper.updateVisibilityLevel(player, newVisibilityLevel);
         } else {
            PlayerHelper.updateVisibilityLevel(player, 0);
         }

      }
   }

   private static int calculateVisibilityLevel(int alertLevel) {
      return alertLevel / 10 * 10;
   }

   public static void mobAlert(EntityTickEvent event) {
      Entity var2 = event.getEntity();
      if (var2 instanceof Mob) {
         Mob mob = (Mob)var2;
         if (!EntityHelper.isAlert(mob) && EntityHelper.getAlertLevel(mob) > 0) {
            EntityHelper.setAlertLevel(mob, EntityHelper.getAlertLevel(mob) - 1);
         }
      } else {
         Entity var3 = event.getEntity();
         if (var3 instanceof AgeableMob) {
            AgeableMob ageableMob = (AgeableMob)var3;
            if (ageableMob.isPanicking()) {
               EntityHelper.setAlert(ageableMob, true);
               EntityHelper.setAlertLevel(ageableMob, 100);
            } else {
               EntityHelper.setAlert(ageableMob, false);
               EntityHelper.setAlertLevel(ageableMob, 0);
            }
         }
      }

   }

   public static void onHitBehavior(LivingIncomingDamageEvent event) {
      Entity var5 = event.getSource().getEntity();
      LivingEntity var6;
      if (var5 instanceof Player) {
         Player player = (Player)var5;
         var6 = event.getEntity();
         if (var6 instanceof Mob) {
            Mob mob = (Mob)var6;
            if (PlayerHelper.getVisibilityLevel(player) > 40) {
               EntityHelper.makeMobRemember(mob, player);
            }

            if (EntityHelper.getAlertLevel(mob) < 100) {
               EntityHelper.setAlertLevel(mob, EntityHelper.getAlertLevel(mob) + 100);
            }

            return;
         }
      }

      var5 = event.getSource().getEntity();
      if (var5 instanceof Mob) {
         Mob mob = (Mob)var5;
         var6 = event.getEntity();
         if (var6 instanceof Player) {
            Player player = (Player)var6;
            EntityHelper.makeMobRemember(mob, player);
         }
      }

   }

   static {
      IS_INVISIBLE_WEIGHT = HackersAndSlashers.serverConfig.IS_INVISIBLE_WEIGHT;
      IS_BEHIND_WEIGHT = HackersAndSlashers.serverConfig.IS_BEHIND_WEIGHT;
      IS_AT_DARK_PLACE = HackersAndSlashers.serverConfig.IS_AT_DARK_PLACE;
      SPRINTING_WEIGHT = HackersAndSlashers.serverConfig.SPRINTING_WEIGHT;
      ON_BUSH_WEIGHT = HackersAndSlashers.serverConfig.ON_BUSH_WEIGHT;
      LAST_ATTACKER_WEIGHT = HackersAndSlashers.serverConfig.LAST_ATTACKER_WEIGHT;
      BEING_SEEN_WEIGHT = HackersAndSlashers.serverConfig.BEING_SEEN_WEIGHT;
      CROUCHING_WEIGHT = HackersAndSlashers.serverConfig.CROUCHING_WEIGHT;
      RAINING_WEIGHT = HackersAndSlashers.serverConfig.RAINING_WEIGHT;
      MOVING_WEIGHT = HackersAndSlashers.serverConfig.MOVING_WEIGHT;
   }
}
