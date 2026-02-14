package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.client.HackersAndSlashersClient;
import net.dndats.hackersandslashers.client.viewport.CameraShake;
import net.dndats.hackersandslashers.client.viewport.DetectionOverlay;
import net.dndats.hackersandslashers.config.StealthoMeterStyle;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent.Pre;
import net.neoforged.neoforge.client.event.ViewportEvent.ComputeCameraAngles;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(
   modid = "hackersandslashers",
   value = {Dist.CLIENT}
)
public class ClientHandler {
   @SubscribeEvent(
      priority = EventPriority.HIGH
   )
   public static void renderOverlay(Pre event) {
      DetectionOverlay.renderDetectionOverlay(event);
   }

   @SubscribeEvent
   public static void cancelCrosshairRender(net.neoforged.neoforge.client.event.RenderGuiLayerEvent.Pre event) {
      if (Minecraft.getInstance().player != null) {
         if (HackersAndSlashersClient.config.stealtho_meter_style == StealthoMeterStyle.CROSSHAIR && Minecraft.getInstance().player.isCrouching() && event.getName() == VanillaGuiLayers.CROSSHAIR) {
            event.setCanceled(true);
         }

      }
   }

   @SubscribeEvent
   public static void renderCameraShake(ComputeCameraAngles event) {
      CameraShake.computeCameraAngles(event);
   }
}
