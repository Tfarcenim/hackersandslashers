package net.dndats.hackersandslashers.client.animations;

import dev.kosmx.playerAnim.api.IPlayable;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import java.util.Objects;
import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.common.network.packets.PacketPlayAnimationAtPlayer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(
   modid = "hackersandslashers",
   bus = Bus.MOD,
   value = {Dist.CLIENT}
)
public class PlayerAnimator {
   @SubscribeEvent
   public static void onClientSetup(FMLClientSetupEvent event) {
      HackersAndSlashers.LOGGER.info("Registering animation factory");
      PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "player_animations"), 42, PlayerAnimator::registerPlayerAnimation);
   }

   private static IAnimation registerPlayerAnimation(AbstractClientPlayer player) {
      return new ModifierLayer();
   }

   public static void playAnimation(LevelAccessor world, Entity entity, String animationName) {
      try {
         if (world.isClientSide()) {
            playClientAnimation(entity, animationName);
         } else {
            playServerAnimation(world, entity, animationName);
         }
      } catch (Exception var4) {
         HackersAndSlashers.LOGGER.error("Error in PlayerAnimator::playAnimation: {}", var4.getMessage(), var4);
      }

   }

   private static void playClientAnimation(Entity entity, String animationName) {
      if (entity instanceof AbstractClientPlayer) {
         Object associatedData = PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer)entity).get(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "player_animations"));
         if (associatedData instanceof ModifierLayer) {
            ModifierLayer<?> modifierLayer = (ModifierLayer)associatedData;
            if (!modifierLayer.isActive()) {
               modifierLayer.replaceAnimationWithFade(AbstractFadeModifier.functionalFadeIn(20, (modelName, type, value) -> {
                  return value;
               }), ((IPlayable)Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath("hackersandslashers", animationName)))).playAnimation().setFirstPersonMode(getFirstPersonAlternatives()).setFirstPersonConfiguration((new FirstPersonConfiguration()).setShowRightArm(false).setShowLeftItem(true)));
            }
         }
      }

   }

   private static FirstPersonMode getFirstPersonAlternatives() {
      return ModList.get().isLoaded("firstperson") ? FirstPersonMode.DISABLED : FirstPersonMode.THIRD_PERSON_MODEL;
   }

   private static void playServerAnimation(LevelAccessor world, Entity entity, String animationName) {
      if (!world.isClientSide() && entity instanceof Player) {
         PacketDistributor.sendToPlayersTrackingEntity(entity, new PacketPlayAnimationAtPlayer(animationName, entity.getId(), false), new CustomPacketPayload[0]);
      }

   }
}
