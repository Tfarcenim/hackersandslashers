package net.dndats.hackersandslashers.events;

import java.util.Iterator;
import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.api.combat.mechanics.stealth.Stealth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;

@EventBusSubscriber(
   modid = "hackersandslashers"
)
public class StealthHandler {
   private static int scheduledTracker = 0;

   @SubscribeEvent
   public static void handleStealthBehavior(LivingChangeTargetEvent event) {
      try {
         Stealth.stealthBehavior(event);
      } catch (Exception var2) {
         HackersAndSlashers.LOGGER.error("Error while trying to cancel entity target: {}", var2.getMessage());
      }

   }

   @SubscribeEvent
   public static void handleOnHitBehavior(LivingIncomingDamageEvent event) {
      try {
         Stealth.onHitBehavior(event);
      } catch (Exception var2) {
         HackersAndSlashers.LOGGER.error("Error while trying to increase alert level on hit: {}", var2.getMessage());
      }

   }

   @SubscribeEvent
   public static void handleMobAlert(Pre event) {
      try {
         Stealth.mobAlert(event);
      } catch (Exception var2) {
         HackersAndSlashers.LOGGER.error("Error while trying to reset entity data (IS_ALERT): {}", var2.getMessage());
      }

   }

   @SubscribeEvent
   public static void resetScheduledTracker(PlayerLoggedInEvent event) {
      scheduledTracker = 0;
   }

   @SubscribeEvent
   public static void mobTargetTracker(net.neoforged.neoforge.event.tick.PlayerTickEvent.Pre event) {
      try {
         if (!event.getEntity().level().isClientSide) {
            ++scheduledTracker;
            if (scheduledTracker >= 5) {
               scheduledTracker = 0;
               Iterator var1 = event.getEntity().level().players().iterator();

               while(var1.hasNext()) {
                  Player player = (Player)var1.next();
                  Stealth.updatePlayerVisibility(player);
               }
            }
         }
      } catch (Exception var3) {
         HackersAndSlashers.LOGGER.error("Error while trying to change player data: {}", var3.getMessage());
      }

   }
}
