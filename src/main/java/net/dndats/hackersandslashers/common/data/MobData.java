package net.dndats.hackersandslashers.common.data;

import java.util.LinkedList;
import java.util.List;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class MobData implements INBTSerializable<CompoundTag> {
   private List<Player> mobMemory = new LinkedList();
   private int alertLevel = 0;
   private boolean isAlert = false;
   private String currentTarget = "";

   public List<Player> getMobMemory() {
      return this.mobMemory;
   }

   public Player getLastMemory() {
      return this.hasMemories() ? (Player)this.mobMemory.getLast() : null;
   }

   public boolean hasMemories() {
      return !this.mobMemory.isEmpty();
   }

   public void clearMemory() {
      this.mobMemory.clear();
   }

   public void forgetMemory(Player player) {
      this.mobMemory.remove(player);
   }

   public void addOnMemory(Player player) {
      if (!this.mobMemory.contains(player)) {
         this.mobMemory.add(player);
         if (this.mobMemory.size() > 3) {
            this.mobMemory.removeFirst();
         }
      }

   }

   public boolean hasOnMemory(Player player) {
      return this.mobMemory.contains(player);
   }

   public int getAlertLevel() {
      return this.alertLevel;
   }

   public void setAlertLevel(int alertLevel) {
      this.alertLevel = alertLevel;
   }

   public boolean isAlert() {
      return this.isAlert;
   }

   public void setAlert(boolean alert) {
      this.isAlert = alert;
   }

   public String getCurrentTarget() {
      return this.currentTarget;
   }

   public void setCurrentTarget(String currentTarget) {
      this.currentTarget = currentTarget;
   }

   @UnknownNullability
   public CompoundTag serializeNBT(@NotNull Provider provider) {
      CompoundTag tag = new CompoundTag();
      tag.putInt("alertLevel", this.alertLevel);
      tag.putBoolean("isAlert", this.isAlert);
      tag.putString("currentTarget", this.currentTarget);
      return tag;
   }

   public void deserializeNBT(@NotNull Provider provider, @NotNull CompoundTag tag) {
      this.alertLevel = tag.getInt("alertLevel");
      this.isAlert = tag.getBoolean("isAlert");
      this.currentTarget = tag.getString("currentTarget");
   }
}
