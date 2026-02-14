package net.dndats.hackersandslashers;

import com.mojang.logging.LogUtils;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.dndats.hackersandslashers.api.combat.critical.logic.HeadshotLogic;
import net.dndats.hackersandslashers.api.combat.critical.logic.RiposteLogic;
import net.dndats.hackersandslashers.api.combat.critical.logic.SneakyAttackLogic;
import net.dndats.hackersandslashers.api.manager.CriticalManager;
import net.dndats.hackersandslashers.api.manager.MeleeCritical;
import net.dndats.hackersandslashers.api.manager.RangedCritical;
import net.dndats.hackersandslashers.common.setup.ModAttributes;
import net.dndats.hackersandslashers.common.setup.ModData;
import net.dndats.hackersandslashers.common.setup.ModMobEffects;
import net.dndats.hackersandslashers.common.setup.ModParticles;
import net.dndats.hackersandslashers.common.setup.ModSounds;
import net.dndats.hackersandslashers.config.ServerConfig;
import net.dndats.hackersandslashers.config.ServerConfigWrapper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod("hackersandslashers")
public class HackersAndSlashers {
   public static final String MODID = "hackersandslashers";
   public static final Logger LOGGER = LogUtils.getLogger();
   public static ServerConfig serverConfig;

   public HackersAndSlashers(IEventBus modEventBus, ModContainer modContainer) {
      modEventBus.addListener(this::commonSetup);
      NeoForge.EVENT_BUS.register(this);
      AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
      serverConfig = ((ServerConfigWrapper)AutoConfig.getConfigHolder(ServerConfigWrapper.class).get()).serverConfig;
      CriticalManager.registerCritical(new MeleeCritical("Riposte", new RiposteLogic(serverConfig.base_riposte_multiplier)));
      CriticalManager.registerCritical(new MeleeCritical("Sneaky Attack", new SneakyAttackLogic(serverConfig.base_sneaky_attack_multiplier)));
      CriticalManager.registerCritical(new RangedCritical("Headshot", new HeadshotLogic(serverConfig.base_headshot_multiplier)));
      ModData.ATTACHMENT_TYPES.register(modEventBus);
      ModMobEffects.register(modEventBus);
      ModSounds.register(modEventBus);
      ModParticles.register(modEventBus);
      ModAttributes.register(modEventBus);
      LOGGER.info("HackersAndSlashers mod initialized without configuration.");
   }

   private void commonSetup(FMLCommonSetupEvent event) {
      LOGGER.info("Common setup executed.");
   }

   @SubscribeEvent
   public void onServerStarting(ServerStartingEvent event) {
      LOGGER.info("Server is starting...");
   }

   @SubscribeEvent
   public void removeVanillaCritical(CriticalHitEvent event) {
      event.setCriticalHit(false);
   }

   @SubscribeEvent
   public void removeInvulnerabilityTicks(LivingIncomingDamageEvent event) {
      Entity var3 = event.getSource().getEntity();
      if (var3 instanceof Player) {
         Player player = (Player)var3;
         event.setInvulnerabilityTicks(0);
      }

   }

   @SubscribeEvent
   public void removeInvisibilityPotionOnHit(LivingIncomingDamageEvent event) {
      if (event.getEntity().hasEffect(MobEffects.INVISIBILITY)) {
         event.getEntity().removeEffect(MobEffects.INVISIBILITY);
      }

   }
}
