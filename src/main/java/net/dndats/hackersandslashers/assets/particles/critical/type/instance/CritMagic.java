package net.dndats.hackersandslashers.assets.particles.critical.type.instance;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CritMagic extends TextureSheetParticle {
   private final SpriteSet spriteSet;

   public CritMagic(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
      super(world, x, y, z);
      this.spriteSet = spriteSet;
      this.setSize(2.0F, 2.0F);
      this.quadSize *= 2.0F;
      this.lifetime = 40;
      this.gravity = -0.1F;
      this.hasPhysics = false;
      this.xd = vx * 1.0D;
      this.yd = vy * 1.0D;
      this.zd = vz * 1.0D;
      this.rCol = 1.0F;
      this.gCol = 0.5F;
      this.bCol = 1.0F;
      this.pickSprite(spriteSet);
   }

   public int getLightColor(float partialTick) {
      int baseLight = 15728880;
      float fadeFactor = 1.0F - (float)this.age / (float)this.lifetime;
      int fadedLight = (int)((float)baseLight * fadeFactor);
      return fadedLight;
   }

   @NotNull
   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_LIT;
   }

   public void tick() {
      super.tick();
      if (this.age < this.lifetime) {
         this.quadSize *= 1.0F - (float)this.age / (float)this.lifetime;
      }

   }
}
