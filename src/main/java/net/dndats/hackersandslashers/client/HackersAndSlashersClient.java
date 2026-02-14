package net.dndats.hackersandslashers.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.dndats.hackersandslashers.config.ClientConfig;
import net.dndats.hackersandslashers.config.ClientConfigWrapper;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(
   modid = "hackersandslashers",
   bus = Bus.MOD,
   value = {Dist.CLIENT}
)
public class HackersAndSlashersClient {
   public static ClientConfig config;

   @SubscribeEvent
   public static void onClientSetup(FMLClientSetupEvent event) {
      AutoConfig.register(ClientConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
      config = ((ClientConfigWrapper)AutoConfig.getConfigHolder(ClientConfigWrapper.class).getConfig()).clientConfig;
      ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> {
         return (modContainer, parent) -> {
            return (Screen)AutoConfig.getConfigScreen(ClientConfigWrapper.class, parent).get();
         };
      });
   }
}
