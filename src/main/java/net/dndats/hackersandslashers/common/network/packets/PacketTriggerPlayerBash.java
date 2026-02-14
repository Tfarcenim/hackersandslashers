package net.dndats.hackersandslashers.common.network.packets;

import java.util.Objects;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.common.data.IsBashingData;
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
public record PacketTriggerPlayerBash(IsBashingData data, int duration) implements CustomPacketPayload {
   public static final Type<PacketTriggerPlayerBash> TYPE = new Type(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "player_bash_sync"));
   public static final StreamCodec<RegistryFriendlyByteBuf, PacketTriggerPlayerBash> STREAM_CODEC = StreamCodec.of((buffer, packet) -> {
      buffer.writeNbt(packet.data().serializeNBT(buffer.registryAccess()));
      buffer.writeInt(packet.duration());
   }, (buffer) -> {
      IsBashingData data = new IsBashingData();
      data.deserializeNBT(buffer.registryAccess(), (CompoundTag)((CompoundTag)Objects.requireNonNull(buffer.readNbt())));
      int duration = buffer.readInt();
      return new PacketTriggerPlayerBash(data, duration);
   });

   public PacketTriggerPlayerBash(IsBashingData data, int duration) {
      this.data = data;
      this.duration = duration;
   }

   public static void handleData(PacketTriggerPlayerBash message, IPayloadContext context) {
      if (context.flow().isServerbound() && message.data() != null) {
         context.enqueueWork(() -> {
            ((IsBashingData)context.player().getData(ModData.IS_BASHING)).deserializeNBT(context.player().registryAccess(), (CompoundTag)message.data().serializeNBT(context.player().registryAccess()));
            context.player().setData(ModData.IS_BASHING, message.data());
            if (message.data.isBashing()) {
               SoundEffects.playBashSwingSound(context.player());
               TickScheduler.schedule(() -> {
                  PlayerHelper.removeSpeedModifier(context.player());
                  ((IsBashingData)Objects.requireNonNull((IsBashingData)context.player().setData(ModData.IS_BASHING, message.data()))).setBashing(false);
                  ((IsBashingData)context.player().getData(ModData.IS_BASHING)).syncData(context.player());
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
   private static void handleClientData(PacketTriggerPlayerBash message) {
      Player player = Minecraft.getInstance().player;
      if (player != null) {
         IsBashingData bashingData = (IsBashingData)player.getData(ModData.IS_BASHING);
         bashingData.deserializeNBT(player.registryAccess(), (CompoundTag)message.data().serializeNBT(player.registryAccess()));
         player.setData(ModData.IS_BASHING, bashingData);
      }

   }

   @SubscribeEvent
   public static void registerMessage(FMLCommonSetupEvent event) {
      NetworkHandler.addNetworkMessage(TYPE, STREAM_CODEC, PacketTriggerPlayerBash::handleData);
   }

   @NotNull
   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public IsBashingData data() {
      return this.data;
   }

   public int duration() {
      return this.duration;
   }
}
