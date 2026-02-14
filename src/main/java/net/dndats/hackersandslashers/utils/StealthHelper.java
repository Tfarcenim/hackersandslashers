package net.dndats.hackersandslashers.utils;

import java.util.Iterator;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class StealthHelper {
   public static boolean isBeingSeen(Player player) {
      if (player == null) {
         return false;
      } else {
         Vec3 surroundings = new Vec3(player.getX(), player.getY(), player.getZ());
         Iterator var2 = player.level().getEntitiesOfClass(Mob.class, (new AABB(surroundings, surroundings)).inflate(32.0D)).iterator();

         Mob mob;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            mob = (Mob)var2.next();
         } while(!EntityHelper.canSee(player, mob));

         return true;
      }
   }

   public static void mobAlertSetter(Player player) {
      if (player != null) {
         Vec3 surroundings = new Vec3(player.getX(), player.getY(), player.getZ());
         player.level().getEntitiesOfClass(Mob.class, (new AABB(surroundings, surroundings)).inflate(32.0D)).forEach((mob) -> {
            if (mob.getPersistentData().contains("is_alert")) {
               EntityHelper.setAlert(mob, EntityHelper.canSee(player, mob));
            }

         });
      }
   }
}
