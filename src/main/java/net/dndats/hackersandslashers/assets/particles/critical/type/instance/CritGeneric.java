package net.dndats.hackersandslashers.assets.particles.critical.type.instance;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CritGeneric extends TextureSheetParticle {
   private final SpriteSet spriteSet;

   public CritGeneric(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
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
      this.rCol = 0.85F;
      this.gCol = 0.85F;
      this.bCol = 1.0F;
      this.pickSprite(spriteSet);
   }

   public int getLightColor(float partialTick) {
      int baseLight = 15728880;
      float fadeFactor = 1.0F - (float)this.age / (float)this.lifetime;
      int fadedLight = (int)((float)baseLight * fadeFactor);
      return fadedLight;
   }

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
