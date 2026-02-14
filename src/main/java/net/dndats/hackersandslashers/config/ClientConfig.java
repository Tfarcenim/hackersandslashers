package net.dndats.hackersandslashers.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;

@Config(
   name = "client"
)
public class ClientConfig implements ConfigData {
   @Tooltip
   public StealthoMeterStyle stealtho_meter_style;
   @Tooltip
   public boolean enable_critical_burst;

   public ClientConfig() {
      this.stealtho_meter_style = StealthoMeterStyle.CROSSHAIR;
      this.enable_critical_burst = true;
   }
}
