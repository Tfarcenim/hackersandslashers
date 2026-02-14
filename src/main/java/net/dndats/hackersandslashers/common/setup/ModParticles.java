package net.dndats.hackersandslashers.common.setup;

import java.util.function.Supplier;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {
   public static final DeferredRegister<ParticleType<?>> PARTICLES;
   public static final Supplier<SimpleParticleType> CRIT_GENERIC;
   public static final Supplier<SimpleParticleType> ATTACK_SPARK_CRIT_GENERIC;
   public static final Supplier<SimpleParticleType> CRIT_MAGIC;
   public static final Supplier<SimpleParticleType> ATTACK_SPARK;

   public static void register(IEventBus eventBus) {
      PARTICLES.register(eventBus);
   }

   static {
      PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, "hackersandslashers");
      CRIT_GENERIC = PARTICLES.register("crit_generic", () -> {
         return new SimpleParticleType(false);
      });
      ATTACK_SPARK_CRIT_GENERIC = PARTICLES.register("attack_spark_crit_generic", () -> {
         return new SimpleParticleType(false);
      });
      CRIT_MAGIC = PARTICLES.register("crit_magic", () -> {
         return new SimpleParticleType(false);
      });
      ATTACK_SPARK = PARTICLES.register("attack_spark", () -> {
         return new SimpleParticleType(false);
      });
   }
}
