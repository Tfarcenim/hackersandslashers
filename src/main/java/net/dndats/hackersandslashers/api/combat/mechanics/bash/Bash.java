package net.dndats.hackersandslashers.api.combat.mechanics.bash;

import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.common.data.IsBashingData;
import net.dndats.hackersandslashers.common.network.packets.PacketTriggerPlayerBash;
import net.dndats.hackersandslashers.common.setup.ModAttributes;
import net.dndats.hackersandslashers.common.setup.ModData;
import net.dndats.hackersandslashers.utils.ItemHelper;
import net.dndats.hackersandslashers.utils.PlayerHelper;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class Bash {
   public static final double BASH_STRENGTH = 0.5D;

   public static int getCooldown(Player player) {
      AttributeInstance cooldown = player.getAttribute(ModAttributes.BASH_RECHARGE);
      return cooldown == null ? 0 : (int)cooldown.getValue();
   }

   public static double getBashStrength() {
      return 0.5D;
   }

   public static void bashBehavior(LivingIncomingDamageEvent event) {
      LivingEntity var2 = event.getEntity();
      if (var2 instanceof Player) {
         Player player = (Player)var2;
         if (PlayerHelper.isBashing(player)) {
            handleBashEffects(player, event);
         }
      }
   }

   private static void handleBashEffects(Player player, LivingIncomingDamageEvent event) {
      SoundEffects.playBashSound(player);
      event.getSource().getEntity();
      Entity var3 = event.getSource().getEntity();
      if (var3 instanceof LivingEntity) {
         LivingEntity source = (LivingEntity)var3;
         applyKnockback(player, source);
      }

   }

   private static void applyKnockback(Player player, LivingEntity attacker) {
      double dx = player.getX() - attacker.getX();
      double dz = player.getZ() - attacker.getZ();
      double distance = Math.sqrt(dx * dx + dz * dz);
      dx /= distance;
      dz /= distance;
      attacker.hurtMarked = true;
      attacker.knockback(getBashStrength(), dx, dz);
      attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 25, 2, false, false));
      attacker.hurt(new DamageSource(player.level().holderOrThrow(DamageTypes.PLAYER_ATTACK), player), (float)((double)(player.getMaxHealth() + player.getAbsorptionAmount()) * 0.1D));
   }

   public static void triggerBashing(int duration, Player player) {
      if (player != null) {
         if (canBash(player)) {
            applyDash(player);
            IsBashingData playerData = (IsBashingData)player.getData(ModData.IS_BASHING);
            playerData.setBashing(true);
            PacketDistributor.sendToServer(new PacketTriggerPlayerBash(playerData, duration), new CustomPacketPayload[0]);
         }

      }
   }

   public static void applyDash(Player player) {
      Vec3 lookDirection = player.getLookAngle();
      Vec3 dashVector = lookDirection.scale(getBashStrength());
      player.setDeltaMovement(player.getDeltaMovement().add(dashVector));
   }

   public static boolean canBash(Player player) {
      return player.isBlocking() && ItemHelper.isHoldingBashItem(player);
   }
}
