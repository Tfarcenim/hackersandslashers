package net.dndats.hackersandslashers.common.network.packets;

import dev.kosmx.playerAnim.api.IPlayable;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import java.util.Objects;
import net.dndats.hackersandslashers.common.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(
   modid = "hackersandslashers",
   bus = Bus.MOD
)
public record PacketPlayAnimationAtPlayer(String animationName, Integer entityId, boolean override) implements CustomPacketPayload {
   public static final Type<PacketPlayAnimationAtPlayer> TYPE = new Type(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "sync_clients_animation"));
   public static final StreamCodec<RegistryFriendlyByteBuf, PacketPlayAnimationAtPlayer> STREAM_CODEC = StreamCodec.of((buffer, message) -> {
      buffer.writeUtf(message.animationName);
      buffer.writeInt(message.entityId);
      buffer.writeBoolean(message.override);
   }, (buffer) -> {
      return new PacketPlayAnimationAtPlayer(buffer.readUtf(), buffer.readInt(), buffer.readBoolean());
   });

   public PacketPlayAnimationAtPlayer(String animationName, Integer entityId, boolean override) {
      this.animationName = animationName;
      this.entityId = entityId;
      this.override = override;
   }

   public static void handleData(PacketPlayAnimationAtPlayer message, IPayloadContext context) {
      if (context.flow().isClientbound()) {
         context.enqueueWork(() -> {
            handleClientData(message);
         }).exceptionally((e) -> {
            context.connection().disconnect(Component.literal(e.getMessage()));
            return null;
         });
      }

   }

   @OnlyIn(Dist.CLIENT)
   private static void handleClientData(PacketPlayAnimationAtPlayer message) {
      Level level = Minecraft.getInstance().level;
      if (Minecraft.getInstance().player != null && level != null) {
         if (level.getEntity(message.entityId()) != null) {
            Player player = (Player)level.getEntity(message.entityId());
            if (player == Minecraft.getInstance().player) {
               return;
            }

            if (player instanceof AbstractClientPlayer) {
               AbstractClientPlayer clientPlayer = (AbstractClientPlayer)player;
               Object associatedData = PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer).get(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "player_animations"));
               if (associatedData instanceof ModifierLayer) {
                  ModifierLayer<?> modifierLayer = (ModifierLayer)associatedData;
                  modifierLayer.replaceAnimationWithFade(AbstractFadeModifier.functionalFadeIn(20, (modelName, type, value) -> {
                     return value;
                  }), ((IPlayable)Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath("hackersandslashers", message.animationName())))).playAnimation().setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration((new FirstPersonConfiguration()).setShowRightArm(false).setShowLeftItem(true)));
               }
            }
         }

      }
   }

   @SubscribeEvent
   public static void registerMessage(FMLCommonSetupEvent event) {
      NetworkHandler.addNetworkMessage(TYPE, STREAM_CODEC, PacketPlayAnimationAtPlayer::handleData);
   }

   @NotNull
   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public String animationName() {
      return this.animationName;
   }

   public Integer entityId() {
      return this.entityId;
   }

   public boolean override() {
      return this.override;
   }
}
