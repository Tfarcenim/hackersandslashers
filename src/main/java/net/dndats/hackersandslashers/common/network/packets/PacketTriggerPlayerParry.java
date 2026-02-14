package net.dndats.hackersandslashers.common.network.packets;

import java.util.Objects;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.common.data.IsParryingData;
import net.dndats.hackersandslashers.common.network.NetworkHandler;
import net.dndats.hackersandslashers.common.setup.ModData;
import net.dndats.hackersandslashers.utils.PlayerHelper;
import net.dndats.hackersandslashers.utils.TickScheduler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
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
public record PacketTriggerPlayerParry(IsParryingData data, int duration) implements CustomPacketPayload {
   public static final Type<PacketTriggerPlayerParry> TYPE = new Type(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "player_parry_sync"));
   public static final StreamCodec<RegistryFriendlyByteBuf, PacketTriggerPlayerParry> STREAM_CODEC = StreamCodec.of((buffer, packet) -> {
      buffer.writeNbt(packet.data().serializeNBT(buffer.registryAccess()));
      buffer.writeInt(packet.duration());
   }, (buffer) -> {
      IsParryingData data = new IsParryingData();
      data.deserializeNBT(buffer.registryAccess(), (CompoundTag)((CompoundTag)Objects.requireNonNull(buffer.readNbt())));
      int duration = buffer.readInt();
      return new PacketTriggerPlayerParry(data, duration);
   });

   public PacketTriggerPlayerParry(IsParryingData data, int duration) {
      this.data = data;
      this.duration = duration;
   }

   public static void handleData(PacketTriggerPlayerParry message, IPayloadContext context) {
      if (context.flow().isServerbound() && message.data() != null) {
         context.enqueueWork(() -> {
            ((IsParryingData)context.player().getData(ModData.IS_PARRYING)).deserializeNBT(context.player().registryAccess(), (CompoundTag)message.data().serializeNBT(context.player().registryAccess()));
            context.player().setData(ModData.IS_PARRYING, message.data());
            if (message.data.getIsParrying()) {
               SoundEffects.playParrySwingSound(context.player());
               PlayerHelper.addSpeedModifier(context.player(), context.player().getMainHandItem());
               TickScheduler.schedule(() -> {
                  PlayerHelper.removeSpeedModifier(context.player());
                  ((IsParryingData)Objects.requireNonNull((IsParryingData)context.player().setData(ModData.IS_PARRYING, message.data()))).setIsParrying(false);
                  ((IsParryingData)context.player().getData(ModData.IS_PARRYING)).syncData(context.player());
               }, message.duration());
            }

         }).exceptionally((e) -> {
            context.connection().disconnect(Component.literal(e.getMessage()));
            return null;
         });
      }

      if (context.flow().isClientbound() && message.data() != null) {
         context.enqueueWork(() -> {
            handleClientData(message);
         }).exceptionally((e) -> {
            context.connection().disconnect(Component.literal(e.getMessage()));
            return null;
         });
      }

   }

   @OnlyIn(Dist.CLIENT)
   private static void handleClientData(PacketTriggerPlayerParry message) {
      Player player = Minecraft.getInstance().player;
      if (player != null) {
         IsParryingData parryingData = (IsParryingData)player.getData(ModData.IS_PARRYING);
         parryingData.deserializeNBT(player.registryAccess(), (CompoundTag)message.data().serializeNBT(player.registryAccess()));
         player.setData(ModData.IS_PARRYING, parryingData);
      }

   }

   @SubscribeEvent
   public static void registerMessage(FMLCommonSetupEvent event) {
      NetworkHandler.addNetworkMessage(TYPE, STREAM_CODEC, PacketTriggerPlayerParry::handleData);
   }

   @NotNull
   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public IsParryingData data() {
      return this.data;
   }

   public int duration() {
      return this.duration;
   }
}
