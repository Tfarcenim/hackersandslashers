package net.dndats.hackersandslashers.api.item;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import net.dndats.hackersandslashers.utils.ItemHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class WeaponCategoryVerifier {
   public static final Set<String> lightSwordWeapons = new HashSet(Arrays.asList("dagger", "knife", "saï", "sai", "sickle", "stiletto", "dirk", "poniard", "switchblade", "shiv"));
   public static final Set<String> genericSwordWeapons = new HashSet(Arrays.asList("sword", "cutlass", "blade", "gladius", "falchion", "broadsword", "sabre", "backsword"));
   public static final Set<String> longSwordWeapons = new HashSet(Arrays.asList("claymore", "longsword", "zweihander", "greatsword", "flamberge", "bastard", "broadsword"));
   public static final Set<String> nimbleSwordWeapons = new HashSet(Arrays.asList("rapier", "saber", "foil", "épée", "estoc", "smallsword", "tuck"));
   public static final Set<String> slicerSwordWeapons = new HashSet(Arrays.asList("katana", "uchigatana", "nodachi", "tachi", "falx", "dao"));
   public static final Set<String> heavySwordWeapons = new HashSet(Arrays.asList("hammer", "axe", "mace", "war", "poleaxe", "maul", "flail", "morningstar", "club", "scythe", "spear", "glaive"));
   public static final Set<String> staffSwordWeapons = new HashSet(Arrays.asList("staff", "quarterstaff", "pole"));

   private static String getParryAnimationName(String mainHandName) {
      Stream var10000 = lightSwordWeapons.stream();
      Objects.requireNonNull(mainHandName);
      if (var10000.anyMatch(mainHandName::contains)) {
         return "parry_short";
      } else {
         var10000 = genericSwordWeapons.stream();
         Objects.requireNonNull(mainHandName);
         if (var10000.anyMatch(mainHandName::contains)) {
            return "parry_generic";
         } else {
            var10000 = longSwordWeapons.stream();
            Objects.requireNonNull(mainHandName);
            if (var10000.anyMatch(mainHandName::contains)) {
               return "parry_long";
            } else {
               var10000 = nimbleSwordWeapons.stream();
               Objects.requireNonNull(mainHandName);
               if (var10000.anyMatch(mainHandName::contains)) {
                  return "parry_slim";
               } else {
                  var10000 = slicerSwordWeapons.stream();
                  Objects.requireNonNull(mainHandName);
                  if (var10000.anyMatch(mainHandName::contains)) {
                     return "parry_slicer";
                  } else {
                     var10000 = heavySwordWeapons.stream();
                     Objects.requireNonNull(mainHandName);
                     if (var10000.anyMatch(mainHandName::contains)) {
                        return "parry_heavy";
                     } else {
                        var10000 = staffSwordWeapons.stream();
                        Objects.requireNonNull(mainHandName);
                        return var10000.anyMatch(mainHandName::contains) ? "parry_staff" : "parry_generic";
                     }
                  }
               }
            }
         }
      }
   }

   public static String getUsingSwordParryAnimation(ItemStack itemStack) {
      return getParryAnimationName(ItemHelper.getRegistryName(itemStack));
   }

   public static WeaponCategory getUsingWeaponCategory(Player player) {
      ItemStack usedItem = player.getWeaponItem();
      if ((double)ItemHelper.getAttackSpeed(usedItem, player) >= 2.25D) {
         return WeaponCategory.LIGHT;
      } else if ((double)ItemHelper.getAttackSpeed(usedItem, player) >= 1.5D) {
         return WeaponCategory.MEDIUM;
      } else {
         return (double)ItemHelper.getAttackSpeed(usedItem, player) < 1.5D ? WeaponCategory.HEAVY : WeaponCategory.MEDIUM;
      }
   }

   public static WeaponCategory getWeaponCategory(ItemStack item) {
      if ((double)ItemHelper.getBaseAttackSpeed(item) >= 2.25D) {
         return WeaponCategory.LIGHT;
      } else if ((double)ItemHelper.getBaseAttackSpeed(item) >= 1.5D) {
         return WeaponCategory.MEDIUM;
      } else {
         return (double)ItemHelper.getBaseAttackSpeed(item) < 1.5D ? WeaponCategory.HEAVY : WeaponCategory.MEDIUM;
      }
   }
}
