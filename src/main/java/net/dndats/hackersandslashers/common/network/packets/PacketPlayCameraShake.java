package net.dndats.hackersandslashers.common.network.packets;

import net.dndats.hackersandslashers.client.viewport.CameraShake;
import net.dndats.hackersandslashers.common.network.NetworkHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
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
public record PacketPlayCameraShake(int duration, float intensity) implements CustomPacketPayload {
   public static final Type<PacketPlayCameraShake> TYPE = new Type(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "player_camera_shake"));
   public static final StreamCodec<RegistryFriendlyByteBuf, PacketPlayCameraShake> STREAM_CODEC = StreamCodec.of((buffer, message) -> {
      buffer.writeInt(message.duration);
      buffer.writeFloat(message.intensity);
   }, (buffer) -> {
      return new PacketPlayCameraShake(buffer.readInt(), buffer.readFloat());
   });

   public PacketPlayCameraShake(int duration, float intensity) {
      this.duration = duration;
      this.intensity = intensity;
   }

   public static void handleData(PacketPlayCameraShake message, IPayloadContext context) {
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
   private static void handleClientData(PacketPlayCameraShake message) {
      CameraShake.triggerCameraShake(message.duration, message.intensity);
   }

   @SubscribeEvent
   public static void registerMessage(FMLCommonSetupEvent event) {
      NetworkHandler.addNetworkMessage(TYPE, STREAM_CODEC, PacketPlayCameraShake::handleData);
   }

   @NotNull
   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public int duration() {
      return this.duration;
   }

   public float intensity() {
      return this.intensity;
   }
}
