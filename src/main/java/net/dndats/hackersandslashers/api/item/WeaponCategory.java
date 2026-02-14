package net.dndats.hackersandslashers.api.item;

public enum WeaponCategory {
   LIGHT("Light"),
   MEDIUM("Medium"),
   HEAVY("Heavy");

   private final String description;

   private WeaponCategory(String param3) {
      this.description = description;
   }

   public String getDescription() {
      return this.description;
   }

   // $FF: synthetic method
   private static WeaponCategory[] $values() {
      return new WeaponCategory[]{LIGHT, MEDIUM, HEAVY};
   }
}
