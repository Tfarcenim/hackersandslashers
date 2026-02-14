package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.api.combat.mechanics.parry.Parry;
import net.dndats.hackersandslashers.common.data.IsParryingData;
import net.dndats.hackersandslashers.common.setup.ModData;
import net.dndats.hackersandslashers.utils.EntityHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@EventBusSubscriber(
   modid = "hackersandslashers"
)
public class ParryHandler {
   @SubscribeEvent
   public static void updateLostHealth(LivingIncomingDamageEvent event) {
      try {
         LivingEntity var2 = event.getEntity();
         if (var2 instanceof Mob) {
            Mob mob = (Mob)var2;
            if (event.getSource().getEntity() instanceof Player) {
               EntityHelper.updateLostHealth(mob);
            }
         }
      } catch (Exception var3) {
         HackersAndSlashers.LOGGER.error("Error while trying to update entity lost health data: {}", var3.getMessage());
      }

   }

   @SubscribeEvent
   public static void handleParryBehavior(LivingIncomingDamageEvent event) {
      try {
         Parry.parryBehavior(25.0F, event);
      } catch (Exception var2) {
         HackersAndSlashers.LOGGER.error("Error while trying to parry an attack: {}", var2.getMessage());
      }

   }

   @SubscribeEvent
   public static void attackCancelDefensiveState(AttackEntityEvent event) {
      Player player = event.getEntity();
      IsParryingData playerData = (IsParryingData)player.getData(ModData.IS_PARRYING);
      if (playerData.getIsParrying()) {
         event.setCanceled(true);
      }

   }
}
