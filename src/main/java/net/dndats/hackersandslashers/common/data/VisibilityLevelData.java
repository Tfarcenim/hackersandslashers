package net.dndats.hackersandslashers.common.data;

import net.dndats.hackersandslashers.common.network.packets.PacketSetPlayerVisibility;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class VisibilityLevelData implements INBTSerializable<CompoundTag> {
   private int visibilityLevel = 0;

   public int getVisibilityLevel() {
      return this.visibilityLevel;
   }

   public void setVisibilityLevel(int visibilityLevel) {
      this.visibilityLevel = visibilityLevel;
   }

   @UnknownNullability
   public CompoundTag serializeNBT(@NotNull Provider provider) {
      CompoundTag tag = new CompoundTag();
      tag.putInt("visibilityLevel", this.visibilityLevel);
      return tag;
   }

   public void deserializeNBT(@NotNull Provider provider, CompoundTag tag) {
      this.visibilityLevel = tag.getInt("visibilityLevel");
   }

   public void syncData(Entity entity) {
      if (entity instanceof ServerPlayer) {
         ServerPlayer serverPlayer = (ServerPlayer)entity;
         PacketDistributor.sendToPlayer(serverPlayer, new PacketSetPlayerVisibility(this), new CustomPacketPayload[0]);
      }

   }
}
