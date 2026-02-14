package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.api.item.WeaponCategoryVerifier;
import net.dndats.hackersandslashers.client.animations.PlayerAnimator;
import net.dndats.hackersandslashers.common.network.packets.PacketServerPlayAnimation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
public class AnimationHelper {
   private static void playAnimation(Player player, String animationKey) {
      PlayerAnimator.playAnimation(player.level(), player, animationKey);
      PacketDistributor.sendToServer(new PacketServerPlayAnimation(animationKey), new CustomPacketPayload[0]);
   }

   public static void playBlockAnimation(Player player) {
      if (ModList.get().isLoaded("bettercombat")) {
         String weaponCategory = WeaponCategoryVerifier.getUsingSwordParryAnimation(player.getMainHandItem());
         if (player.getMainHandItem().getItem() instanceof SwordItem && player.getOffhandItem().getItem() instanceof SwordItem) {
            playAnimation(player, weaponCategory + "_dh");
         } else if (player.getMainHandItem().getItem() instanceof SwordItem) {
            playAnimation(player, weaponCategory + "_oh");
         } else {
            playAnimation(player, weaponCategory + "_oh");
         }
      } else {
         playAnimation(player, "parry_generic_oh");
      }

   }

   public static void playBashAnimation(Player player) {
      if (player.getMainHandItem().getItem() instanceof ShieldItem) {
         playAnimation(player, "bash_generic_rh");
      } else {
         playAnimation(player, "bash_generic_lh");
      }

   }
}
