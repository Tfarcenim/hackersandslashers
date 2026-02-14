package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.api.manager.CriticalManager;
import net.dndats.hackersandslashers.client.HackersAndSlashersClient;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.client.effects.VisualEffects;
import net.dndats.hackersandslashers.common.network.packets.PacketPlayCameraShake;
import net.dndats.hackersandslashers.utils.CombatHelper;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(
   modid = "hackersandslashers"
)
public class CriticalHandler {
   @SubscribeEvent
   public static void dealCriticalHit(LivingIncomingDamageEvent event) {
      try {
         if (event.getSource().getEntity() != null) {
            boolean isCritical = false;
            Entity var3 = event.getSource().getEntity();
            if (var3 instanceof Player) {
               Player player = (Player)var3;
               isCritical = CriticalManager.applyCriticalHit(event);
               if (isCritical) {
                  if (HackersAndSlashersClient.config.enable_critical_burst) {
                     PacketDistributor.sendToPlayer((ServerPlayer)player, new PacketPlayCameraShake(10, 1.0F), new CustomPacketPayload[0]);
                  }

                  SoundEffects.playCriticalSound(event.getEntity());
                  VisualEffects.spawnCriticalParticle(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getEyeY() + 1.0D, event.getEntity().getZ(), event.getSource());
               }
            }

            if (event.getSource().getEntity() != null) {
               CombatHelper.spawnCombatParticles(event, isCritical);
            }
         }
      } catch (Exception var4) {
         HackersAndSlashers.LOGGER.error("Error while trying to apply a critical hit: {}", var4.getMessage());
      }

   }
}
