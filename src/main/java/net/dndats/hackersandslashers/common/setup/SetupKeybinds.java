package net.dndats.hackersandslashers.common.setup;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(
   modid = "hackersandslashers",
   bus = Bus.MOD,
   value = {Dist.CLIENT}
)
public class SetupKeybinds {
   @SubscribeEvent
   public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
      event.register(ModKeybinds.PARRY);
   }
}
