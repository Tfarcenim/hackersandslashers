package net.dndats.hackersandslashers.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
   name = "server"
)
public class ServerConfig implements ConfigData {
   @Comment("Changes the base riposte critical multiplier")
   public float base_riposte_multiplier = 1.25F;
   @Comment("Changes the base sneaky attack critical multiplier")
   public float base_sneaky_attack_multiplier = 1.5F;
   @Comment("Changes the base headshot critical multiplier")
   public float base_headshot_multiplier = 1.75F;
   @Comment("Disables the weapon type based critical modifier")
   public boolean enable_additional_multipliers = true;
   @Comment("Changes the Stealth system environmental weights:")
   public int IS_INVISIBLE_WEIGHT = 80;
   public int IS_BEHIND_WEIGHT = 70;
   public int IS_AT_DARK_PLACE = 50;
   public int SPRINTING_WEIGHT = 70;
   public int ON_BUSH_WEIGHT = 60;
   public int LAST_ATTACKER_WEIGHT = 40;
   public int BEING_SEEN_WEIGHT = 40;
   public int CROUCHING_WEIGHT = 25;
   public int RAINING_WEIGHT = 15;
   public int MOVING_WEIGHT = 10;
}
