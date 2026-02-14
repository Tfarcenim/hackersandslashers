package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.api.combat.mechanics.parry.Parry;
import net.dndats.hackersandslashers.common.setup.ModKeybinds;
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
public class ParryKeybindHandler {
   private static int currentParryCooldown = 0;

   @SubscribeEvent
   public static void onEntityParry(Pre event) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player != null) {
         if (Parry.canParry(player) && currentParryCooldown < Parry.getCooldown(player)) {
            ++currentParryCooldown;
         }

         if (ModKeybinds.PARRY.consumeClick() && Parry.canParry(player) && currentParryCooldown >= Parry.getCooldown(player)) {
            currentParryCooldown = 0;
            AnimationHelper.playBlockAnimation(player);
            Parry.triggerDefensive(10, player);
         }

      }
   }
}
