package net.dndats.hackersandslashers.utils;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.dndats.hackersandslashers.client.HackersAndSlashersClient;
import net.dndats.hackersandslashers.config.StealthoMeterStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderGuiEvent.Pre;

@OnlyIn(Dist.CLIENT)
public class OverlayHelper {
   public static void renderOverlay(Pre event, String filepath, Player player) {
      Window window = Minecraft.getInstance().getWindow();
      double scale = window.getGuiScale();
      int width = (int)((double)window.getScreenWidth() / scale);
      int height = (int)((double)window.getScreenHeight() / scale);
      if (player != null) {
         RenderSystem.disableDepthTest();
         RenderSystem.depthMask(false);
         RenderSystem.enableBlend();
         RenderSystem.setShader(GameRenderer::getPositionTexShader);
         RenderSystem.blendFuncSeparate(SourceFactor.ONE_MINUS_DST_COLOR, DestFactor.ONE_MINUS_SRC_COLOR, SourceFactor.ONE, DestFactor.ZERO);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.75F);
         int textureSize = 15;
         int halfTexture = textureSize / 2;
         int posX;
         int posY;
         if (HackersAndSlashersClient.config.stealtho_meter_style == StealthoMeterStyle.CROSSHAIR) {
            posX = width / 2 - halfTexture - 1;
            posY = height / 2 - textureSize + 7;
         } else {
            posX = width / 2 - halfTexture;
            posY = height / 2 - textureSize - 30;
         }

         event.getGuiGraphics().blit(ResourceLocation.parse(filepath), posX, posY, 0.0F, 0.0F, textureSize, textureSize, textureSize, textureSize);
         RenderSystem.depthMask(true);
         RenderSystem.defaultBlendFunc();
         RenderSystem.enableDepthTest();
         RenderSystem.disableBlend();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }
}
