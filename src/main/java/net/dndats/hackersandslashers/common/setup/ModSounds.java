package net.dndats.hackersandslashers.common.setup;

import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
   public static final DeferredRegister<SoundEvent> SOUND_EVENTS;
   public static final Supplier<SoundEvent> PLAYER_CRITICAL;

   private static Supplier<SoundEvent> registerSoundEvent() {
      ResourceLocation id = ResourceLocation.fromNamespaceAndPath("hackersandslashers", "player_critical");
      return SOUND_EVENTS.register("player_critical", () -> {
         return SoundEvent.createVariableRangeEvent(id);
      });
   }

   public static void register(IEventBus eventBus) {
      SOUND_EVENTS.register(eventBus);
   }

   static {
      SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, "hackersandslashers");
      PLAYER_CRITICAL = registerSoundEvent();
   }
}
