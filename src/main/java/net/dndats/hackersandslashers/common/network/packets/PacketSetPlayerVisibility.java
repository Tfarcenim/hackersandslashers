package net.dndats.hackersandslashers.common.network.packets;

import java.util.Objects;
import net.dndats.hackersandslashers.common.data.VisibilityLevelData;
import net.dndats.hackersandslashers.common.network.NetworkHandler;
import net.dndats.hackersandslashers.common.setup.ModData;
import net.minecraft.nbt.CompoundTag;
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
public record PacketSetPlayerVisibility(VisibilityLevelData data) implements CustomPacketPayload {
   public static final Type<PacketSetPlayerVisibility> TYPE = new Type(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "player_visibility_sync"));
   public static final StreamCodec<RegistryFriendlyByteBuf, PacketSetPlayerVisibility> STREAM_CODEC = StreamCodec.of((buffer, packet) -> {
      buffer.writeNbt(packet.data().serializeNBT(buffer.registryAccess()));
   }, (buffer) -> {
      PacketSetPlayerVisibility message = new PacketSetPlayerVisibility(new VisibilityLevelData());
      message.data().deserializeNBT(buffer.registryAccess(), (CompoundTag)((CompoundTag)Objects.requireNonNull(buffer.readNbt())));
      return message;
   });

   public PacketSetPlayerVisibility(VisibilityLevelData data) {
      this.data = data;
   }

   public static void handleData(PacketSetPlayerVisibility message, IPayloadContext context) {
      if (context.flow().isClientbound() && message.data() != null) {
         context.enqueueWork(() -> {
            ((VisibilityLevelData)context.player().getData(ModData.VISIBILITY_LEVEL)).deserializeNBT(context.player().registryAccess(), (CompoundTag)message.data().serializeNBT(context.player().registryAccess()));
            context.player().setData(ModData.VISIBILITY_LEVEL, message.data());
         }).exceptionally((e) -> {
            context.connection().disconnect(Component.literal(e.getMessage()));
            return null;
         });
      }

   }

   @SubscribeEvent
   public static void registerMessage(FMLCommonSetupEvent event) {
      NetworkHandler.addNetworkMessage(TYPE, STREAM_CODEC, PacketSetPlayerVisibility::handleData);
   }

   @NotNull
   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public VisibilityLevelData data() {
      return this.data;
   }
}
