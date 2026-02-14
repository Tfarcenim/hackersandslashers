package net.dndats.hackersandslashers.api.combat.mechanics.ai.goals;

import java.util.EnumSet;
import net.dndats.hackersandslashers.common.data.MobData;
import net.dndats.hackersandslashers.common.setup.ModData;
import net.dndats.hackersandslashers.utils.EntityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.player.Player;

public class MobSearchTargetGoal extends Goal {
   private final PathfinderMob mob;
   private Player target;
   private BlockPos targetPos;
   private double searchSpeed = 1.0D;

   public MobSearchTargetGoal(PathfinderMob mob) {
      this.mob = mob;
      this.setFlags(EnumSet.of(Flag.MOVE));
   }

   public boolean canUse() {
      return this.mob.hasData(ModData.MOB_DETECTABILITY) && EntityHelper.hasMobMemories(this.mob) && !EntityHelper.isAlert(this.mob) || EntityHelper.getAlertLevel(this.mob) > 40;
   }

   public boolean isInterruptable() {
      return false;
   }

   public void start() {
      this.target = ((MobData)this.mob.getData(ModData.MOB_DETECTABILITY)).getLastMemory();
      if (this.target != null) {
         this.targetPos = this.target.blockPosition();
         double offsetX = (double)this.mob.getRandom().nextInt(-3, 3);
         double offsetZ = (double)this.mob.getRandom().nextInt(-3, 3);
         this.mob.getNavigation().moveTo((double)this.targetPos.getX() + offsetX, (double)this.targetPos.getY(), (double)this.targetPos.getZ() + offsetZ, this.searchSpeed);
      }

   }

   public void tick() {
      double distance = (double)EntityHelper.getDistanceBetweenEntities(this.target, this.mob);
      if (distance > 15.0D) {
         this.mob.getNavigation().setSpeedModifier(1.1D);
      } else if (distance > 5.0D) {
         this.mob.getNavigation().setSpeedModifier(1.0D);
      } else {
         this.mob.getNavigation().setSpeedModifier(0.8D);
      }

   }

   public boolean canContinueToUse() {
      return !EntityHelper.isAlert(this.mob) && this.mob.getNavigation().isInProgress();
   }

   public void stop() {
      this.targetPos = null;
      EntityHelper.clearMobMemories(this.mob);
   }
}
