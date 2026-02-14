package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.api.combat.mechanics.bash.Bash;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(
   modid = "hackersandslashers"
)
public class BashHandler {
   @SubscribeEvent
   public static void handleBashBehavior(LivingIncomingDamageEvent event) {
      try {
         Bash.bashBehavior(event);
      } catch (Exception var2) {
         HackersAndSlashers.LOGGER.error("Error while trying to bash an attack: {}", var2.getMessage());
      }

   }
}
