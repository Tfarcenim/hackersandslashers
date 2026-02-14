package net.dndats.hackersandslashers.assets.effects.behavior;

import net.dndats.hackersandslashers.common.setup.ModMobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent.Expired;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent.Remove;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@EventBusSubscriber(
   modid = "hackersandslashers"
)
public class StunBehavior {
   @SubscribeEvent
   public static void disablePlayerActions(AttackEntityEvent event) {
      if (event.getEntity().hasEffect(ModMobEffects.STUN)) {
         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public static void onEffectEnd(Expired event) {
      if (event.getEffectInstance() != null) {
         if (event.getEffectInstance().getEffect().equals(ModMobEffects.STUN)) {
            LivingEntity var2 = event.getEntity();
            if (var2 instanceof Mob) {
               Mob mob = (Mob)var2;
               mob.setNoAi(false);
            }
         }

      }
   }

   @SubscribeEvent
   public static void onEffectRemoved(Remove event) {
      if (event.getEffectInstance() != null) {
         if (event.getEffectInstance().getEffect().equals(ModMobEffects.STUN)) {
            LivingEntity var2 = event.getEntity();
            if (var2 instanceof Mob) {
               Mob mob = (Mob)var2;
               mob.setNoAi(false);
            }
         }

      }
   }
}
