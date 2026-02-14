package net.dndats.hackersandslashers.assets.particles.critical.type.providers;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.assets.particles.critical.type.instance.CritMagic;
import net.dndats.hackersandslashers.common.setup.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(
   bus = Bus.MOD,
   value = {Dist.CLIENT}
)
public class CritMagicProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet spriteSet;

   public CritMagicProvider(SpriteSet spriteSet) {
      this.spriteSet = spriteSet;
   }

   @Nullable
   public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      return new CritMagic(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
   }

   @SubscribeEvent
   public static void register(RegisterParticleProvidersEvent event) {
      HackersAndSlashers.LOGGER.info("Registering particle: Crit magic");
      event.registerSpriteSet((ParticleType)ModParticles.CRIT_MAGIC.get(), CritMagicProvider::new);
   }
}
