package net.dndats.hackersandslashers.common.setup;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(
   bus = Bus.MOD
)
public class ModAttributes {
   public static final DeferredRegister<Attribute> ATTRIBUTES;
   public static final DeferredHolder<Attribute, Attribute> SNEAKINESS;
   public static final DeferredHolder<Attribute, Attribute> SNEAKY_ATTACK_POWER;
   public static final DeferredHolder<Attribute, Attribute> RIPOSTE_POWER;
   public static final DeferredHolder<Attribute, Attribute> HEADSHOT_POWER;
   public static final DeferredHolder<Attribute, Attribute> BASH_RECHARGE;
   public static final DeferredHolder<Attribute, Attribute> PARRY_RECHARGE;

   @SubscribeEvent
   public static void addAttributes(EntityAttributeModificationEvent event) {
      event.add(EntityType.PLAYER, SNEAKINESS);
      event.add(EntityType.PLAYER, SNEAKY_ATTACK_POWER);
      event.add(EntityType.PLAYER, RIPOSTE_POWER);
      event.add(EntityType.PLAYER, HEADSHOT_POWER);
      event.add(EntityType.PLAYER, BASH_RECHARGE);
      event.add(EntityType.PLAYER, PARRY_RECHARGE);
   }

   public static void register(IEventBus eventBus) {
      ATTRIBUTES.register(eventBus);
   }

   static {
      ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, "hackersandslashers");
      SNEAKINESS = ATTRIBUTES.register("sneakiness", () -> {
         return (new RangedAttribute("attribute.hackersandslashers.sneakiness", 0.0D, -100.0D, 100.0D)).setSyncable(true);
      });
      SNEAKY_ATTACK_POWER = ATTRIBUTES.register("sneaky_attack_power", () -> {
         return (new RangedAttribute("attribute.hackersandslashers.sneaky_attack_power", 0.0D, -1024.0D, 1024.0D)).setSyncable(true);
      });
      RIPOSTE_POWER = ATTRIBUTES.register("riposte_power", () -> {
         return (new RangedAttribute("attribute.hackersandslashers.riposte_power", 0.0D, -1024.0D, 1024.0D)).setSyncable(true);
      });
      HEADSHOT_POWER = ATTRIBUTES.register("headshot_power", () -> {
         return (new RangedAttribute("attribute.hackersandslashers.headshot_power", 0.0D, -1024.0D, 1024.0D)).setSyncable(true);
      });
      BASH_RECHARGE = ATTRIBUTES.register("bash_recharge", () -> {
         return (new RangedAttribute("attribute.hackersandslashers.bash_recharge", 20.0D, 10.0D, 120.0D)).setSyncable(true);
      });
      PARRY_RECHARGE = ATTRIBUTES.register("parry_recharge", () -> {
         return (new RangedAttribute("attribute.hackersandslashers.parry_recharge", 20.0D, 10.0D, 120.0D)).setSyncable(true);
      });
   }
}
