package net.dndats.hackersandslashers.common.network;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(
   modid = "hackersandslashers",
   bus = Bus.MOD
)
public class NetworkHandler {
   private static boolean networkingRegistered = false;
   private static final Map<Type<?>, NetworkHandler.NetworkMessage<?>> MESSAGES = new HashMap();

   public static <T extends CustomPacketPayload> void addNetworkMessage(Type<T> id, StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
      if (networkingRegistered) {
         throw new IllegalStateException("Cannot register new network messages after networking has been registered");
      } else {
         MESSAGES.put(id, new NetworkHandler.NetworkMessage(reader, handler));
      }
   }

   @SubscribeEvent
   public static void registerNetworking(RegisterPayloadHandlersEvent event) {
      PayloadRegistrar registrar = event.registrar("hackersandslashers");
      MESSAGES.forEach((id, networkMessage) -> {
         registrar.playBidirectional(id, networkMessage.reader(), networkMessage.handler());
      });
      networkingRegistered = true;
   }

   private static record NetworkMessage<T extends CustomPacketPayload>(StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
      private NetworkMessage(StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
         this.reader = reader;
         this.handler = handler;
      }

      public StreamCodec<? extends FriendlyByteBuf, T> reader() {
         return this.reader;
      }

      public IPayloadHandler<T> handler() {
         return this.handler;
      }
   }
}
