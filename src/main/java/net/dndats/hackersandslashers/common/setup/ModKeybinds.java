package net.dndats.hackersandslashers.common.setup;

import com.mojang.blaze3d.platform.InputConstants.Type;
import net.minecraft.client.KeyMapping;

public class ModKeybinds {
   public static final KeyMapping PARRY;

   static {
      PARRY = new KeyMapping("key.hackersandslashers.parry", Type.MOUSE, 1, "key.categories.hackersandslashers");
   }
}
