package net.dndats.hackersandslashers.api.interfaces;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public interface ICriticalLogic {
   boolean canBeApplied(Entity var1, LivingEntity var2);

   float getAdditionalModifiers(LivingIncomingDamageEvent var1);

   void applyOnHitFunction(LivingIncomingDamageEvent var1);

   float getDamageMultiplier();
}
