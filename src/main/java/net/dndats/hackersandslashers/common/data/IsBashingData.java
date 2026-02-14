package net.dndats.hackersandslashers.common.data;

import net.dndats.hackersandslashers.common.network.packets.PacketTriggerPlayerBash;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class IsBashingData implements INBTSerializable<CompoundTag> {
   public boolean isBashing = false;
   private int duration = 0;

   public boolean isBashing() {
      return this.isBashing;
   }

   public void setBashing(boolean bashing) {
      this.isBashing = bashing;
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   @UnknownNullability
   public CompoundTag serializeNBT(@NotNull Provider provider) {
      CompoundTag tag = new CompoundTag();
      tag.putBoolean("isBashing", this.isBashing);
      tag.putInt("duration", this.duration);
      return tag;
   }

   public void deserializeNBT(@NotNull Provider provider, CompoundTag tag) {
      this.isBashing = tag.getBoolean("isBashing");
      this.duration = tag.getInt("duration");
   }

   public void syncData(Entity entity) {
      if (entity instanceof ServerPlayer) {
         ServerPlayer serverPlayer = (ServerPlayer)entity;
         PacketDistributor.sendToPlayer(serverPlayer, new PacketTriggerPlayerBash(this, this.duration), new CustomPacketPayload[0]);
      }

   }
}
