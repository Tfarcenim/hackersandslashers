package net.dndats.hackersandslashers.api.combat.mechanics.ai;

import net.dndats.hackersandslashers.api.combat.mechanics.ai.goals.MobSearchTargetGoal;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(
   modid = "hackersandslashers"
)
public class MobAIInjector {
   @SubscribeEvent
   public static void addMonsterAiGoals(EntityJoinLevelEvent event) {
      Entity var2 = event.getEntity();
      if (var2 instanceof PathfinderMob) {
         PathfinderMob mob = (PathfinderMob)var2;
         mob.goalSelector.addGoal(100, new MobSearchTargetGoal(mob));
      }

   }
}
