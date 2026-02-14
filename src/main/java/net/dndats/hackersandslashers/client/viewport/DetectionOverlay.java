package net.dndats.hackersandslashers.client.viewport;

import net.dndats.hackersandslashers.client.HackersAndSlashersClient;
import net.dndats.hackersandslashers.config.StealthoMeterStyle;
import net.dndats.hackersandslashers.utils.OverlayHelper;
import net.dndats.hackersandslashers.utils.PlayerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderGuiEvent.Pre;

@OnlyIn(Dist.CLIENT)
public class DetectionOverlay {
   public static void renderDetectionOverlay(Pre event) {
      if (HackersAndSlashersClient.config.stealtho_meter_style != StealthoMeterStyle.DISABLED) {
         Player player = Minecraft.getInstance().player;
         if (player == null || !canRender(player)) {
            return;
         }

         int visibilityLevel = PlayerHelper.getVisibilityLevel(player);
         String texturePath = getOverlayTexture(visibilityLevel);
         OverlayHelper.renderOverlay(event, texturePath, player);
      }

   }

   private static boolean canRender(Player player) {
      return player.isCrouching();
   }

   private static String getOverlayTexture(int visibilityLevel) {
      int[] thresholds = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90};
      String basePath;
      if (HackersAndSlashersClient.config.stealtho_meter_style == StealthoMeterStyle.CROSSHAIR) {
         basePath = "hackersandslashers:textures/screens/crosshair/alert_";
      } else {
         basePath = "hackersandslashers:textures/screens/legacy/alert_";
      }

      int[] var3 = thresholds;
      int var4 = thresholds.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int threshold = var3[var5];
         if (visibilityLevel <= threshold) {
            return basePath + threshold + ".png";
         }
      }

      return basePath + "90.png";
   }
}
