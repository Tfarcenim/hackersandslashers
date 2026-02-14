package net.dndats.hackersandslashers.client.viewport;

import java.util.Random;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.ViewportEvent.ComputeCameraAngles;

@OnlyIn(Dist.CLIENT)
public class CameraShake {
   private static int shakeDuration = 0;
   private static float shakeIntensity = 0.0F;
   private static float currentIntensity = 0.0F;
   private static final Random random = new Random();

   public static void triggerCameraShake(int duration, float intensity) {
      shakeDuration = duration;
      shakeIntensity = intensity;
      currentIntensity = intensity;
   }

   public static void computeCameraAngles(ComputeCameraAngles event) {
      if (shakeDuration > 0) {
         currentIntensity = shakeIntensity * ((float)shakeDuration / (float)(shakeDuration + 1));
         float pitchOffset = (float)Math.sin(random.nextDouble() * 3.141592653589793D * 2.0D) * currentIntensity;
         float yawOffset = (float)Math.sin(random.nextDouble() * 3.141592653589793D * 2.0D) * currentIntensity;
         float rollOffset = (float)Math.sin(random.nextDouble() * 3.141592653589793D * 2.0D) * currentIntensity;
         event.setPitch(event.getPitch() + pitchOffset);
         event.setYaw(event.getYaw() + yawOffset);
         event.setRoll(event.getRoll() + rollOffset);
         --shakeDuration;
      }

   }
}
