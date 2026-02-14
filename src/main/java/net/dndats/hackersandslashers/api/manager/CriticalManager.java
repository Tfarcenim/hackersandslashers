package net.dndats.hackersandslashers.api.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.api.interfaces.ICritical;
import net.dndats.hackersandslashers.utils.CombatHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class CriticalManager {
   private static final float MULTIPLIER_CAP = 50.0F;
   private static final List<ICritical> criticalTypes = new ArrayList();

   public static void registerCritical(ICritical critical) {
      criticalTypes.add(critical);
   }

   public static boolean applyCriticalHit(LivingIncomingDamageEvent event) {
      try {
         Entity var2 = event.getSource().getEntity();
         if (var2 instanceof Player) {
            Player player = (Player)var2;
            float totalDamageMultiplier = Math.min(processCriticalHitMultiplier(event, player), 50.0F);
            if (totalDamageMultiplier > 0.0F) {
               float finalAmount = CombatHelper.dealCriticalDamage(totalDamageMultiplier, event);
               HackersAndSlashers.LOGGER.info("Dealt {} with multiplier of {}", finalAmount, totalDamageMultiplier);
               return true;
            }

            return false;
         }
      } catch (Exception var4) {
         HackersAndSlashers.LOGGER.error("Error while trying to process critical: {}", var4.getMessage());
      }

      return false;
   }

   private static float processCriticalHitMultiplier(LivingIncomingDamageEvent event, Player player) {
      float totalDamageMultiplier = 0.0F;
      Iterator var3 = criticalTypes.iterator();

      while(var3.hasNext()) {
         ICritical critical = (ICritical)var3.next();
         if (critical instanceof RangedCritical) {
            RangedCritical rangedCritical = (RangedCritical)critical;
            if (rangedCritical.getLogic().canBeApplied(event.getSource().getDirectEntity(), event.getEntity())) {
               totalDamageMultiplier += rangedCritical.getLogic().getDamageMultiplier();
               if (HackersAndSlashers.serverConfig.enable_additional_multipliers) {
                  totalDamageMultiplier += critical.getLogic().getAdditionalModifiers(event);
               }

               rangedCritical.getLogic().applyOnHitFunction(event);
            }
         }

         if (critical.getLogic().canBeApplied(player, event.getEntity())) {
            totalDamageMultiplier += critical.getLogic().getDamageMultiplier();
            if (HackersAndSlashers.serverConfig.enable_additional_multipliers) {
               totalDamageMultiplier += critical.getLogic().getAdditionalModifiers(event);
            }

            critical.getLogic().applyOnHitFunction(event);
         }
      }

      return totalDamageMultiplier;
   }
}
