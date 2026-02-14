package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.api.combat.mechanics.bash.Bash;
import net.dndats.hackersandslashers.client.effects.VisualEffects;
import net.dndats.hackersandslashers.utils.AnimationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent.Pre;

@EventBusSubscriber(
   modid = "hackersandslashers",
   value = {Dist.CLIENT}
)
public class BashKeybindHandler {
   private static int currentBashCooldown = 0;

   @SubscribeEvent
   public static void handleBashBehavior(Pre event) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player != null) {
         if (Bash.canBash(player)) {
            if (Bash.canBash(player) && currentBashCooldown < Bash.getCooldown(player)) {
               ++currentBashCooldown;
            }

            if (Minecraft.getInstance().options.keyAttack.consumeClick() && Bash.canBash(player) && currentBashCooldown >= Bash.getCooldown(player)) {
               currentBashCooldown = 0;
               AnimationHelper.playBashAnimation(player);
               VisualEffects.spawnBashParticles(player.level(), player.getX(), player.getY(), player.getZ());
               Bash.triggerBashing(5, player);
            }

         }
      }
   }
}
