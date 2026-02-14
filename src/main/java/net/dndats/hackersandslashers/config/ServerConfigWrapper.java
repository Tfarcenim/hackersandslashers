package net.dndats.hackersandslashers.config;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer.GlobalData;

@Config(
   name = "hackersandslashers"
)
public class ServerConfigWrapper extends GlobalData {
   @Category("server")
   @Excluded
   public ServerConfig serverConfig = new ServerConfig();
}
