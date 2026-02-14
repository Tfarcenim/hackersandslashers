package net.dndats.hackersandslashers.api.manager;

import net.dndats.hackersandslashers.api.interfaces.ICritical;
import net.dndats.hackersandslashers.api.interfaces.ICriticalLogic;

public class MeleeCritical implements ICritical {
   private final String NAME;
   private final ICriticalLogic LOGIC;

   public MeleeCritical(String name, ICriticalLogic logic) {
      this.NAME = name;
      this.LOGIC = logic;
   }

   public String getName() {
      return this.NAME;
   }

   public ICriticalLogic getLogic() {
      return this.LOGIC;
   }
}
