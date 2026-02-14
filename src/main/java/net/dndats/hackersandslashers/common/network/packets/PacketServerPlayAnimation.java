package net.dndats.hackersandslashers.common.network.packets;

import net.dndats.hackersandslashers.client.animations.PlayerAnimator;
import net.dndats.hackersandslashers.common.network.NetworkHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
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
public record PacketServerPlayAnimation(String animationName) implements CustomPacketPayload {
   public static final Type<PacketServerPlayAnimation> TYPE = new Type(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "sync_server_animation"));
   public static final StreamCodec<RegistryFriendlyByteBuf, PacketServerPlayAnimation> STREAM_CODEC = StreamCodec.of((buffer, message) -> {
      buffer.writeUtf(message.animationName());
   }, (buffer) -> {
      return new PacketServerPlayAnimation(buffer.readUtf());
   });

   public PacketServerPlayAnimation(String animationName) {
      this.animationName = animationName;
   }

   public static void handleData(PacketServerPlayAnimation message, IPayloadContext context) {
      if (context.flow().isServerbound()) {
         context.enqueueWork(() -> {
            if (!context.player().level().isClientSide()) {
               PlayerAnimator.playAnimation(context.player().level(), context.player(), message.animationName());
            }

         }).exceptionally((e) -> {
            context.connection().disconnect(Component.literal(e.getMessage()));
            return null;
         });
      }

   }

   @SubscribeEvent
   public static void registerMessage(FMLCommonSetupEvent event) {
      NetworkHandler.addNetworkMessage(TYPE, STREAM_CODEC, PacketServerPlayAnimation::handleData);
   }

   @NotNull
   public Type<PacketServerPlayAnimation> type() {
      return TYPE;
   }

   public String animationName() {
      return this.animationName;
   }
}
