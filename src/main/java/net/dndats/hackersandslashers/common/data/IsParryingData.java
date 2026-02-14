package net.dndats.hackersandslashers.common.data;

import net.dndats.hackersandslashers.common.network.packets.PacketTriggerPlayerParry;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class IsParryingData implements INBTSerializable<CompoundTag> {
   private boolean isParrying = false;
   private int duration = 0;

   public boolean getIsParrying() {
      return this.isParrying;
   }

   public void setIsParrying(boolean isParrying) {
      this.isParrying = isParrying;
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
      tag.putBoolean("isParrying", this.isParrying);
      tag.putInt("duration", this.duration);
      return tag;
   }

   public void deserializeNBT(@NotNull Provider provider, CompoundTag tag) {
      this.isParrying = tag.getBoolean("isParrying");
      this.duration = tag.getInt("duration");
   }

   public void syncData(Entity entity) {
      if (entity instanceof ServerPlayer) {
         ServerPlayer serverPlayer = (ServerPlayer)entity;
         PacketDistributor.sendToPlayer(serverPlayer, new PacketTriggerPlayerParry(this, this.duration), new CustomPacketPayload[0]);
      }

   }
}
