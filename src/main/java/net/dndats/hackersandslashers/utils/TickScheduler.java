package net.dndats.hackersandslashers.utils;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.event.tick.ServerTickEvent.Post;

@EventBusSubscriber(
   modid = "hackersandslashers"
)
public class TickScheduler {
   private static final Queue<TickScheduler.ScheduledTask> workQueue = new ConcurrentLinkedQueue();

   public static void schedule(Runnable action, int ticks) {
      if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
         workQueue.add(new TickScheduler.ScheduledTask(action, ticks));
      }

   }

   @SubscribeEvent
   public static void tick(Post event) {
      Iterator iterator = workQueue.iterator();

      while(iterator.hasNext()) {
         TickScheduler.ScheduledTask task = (TickScheduler.ScheduledTask)iterator.next();
         task.decrementTicks();
         if (task.isReady()) {
            task.run();
            iterator.remove();
         }
      }

   }

   private static class ScheduledTask {
      private final Runnable action;
      private int ticksRemaining;

      public ScheduledTask(Runnable action, int ticks) {
         this.action = action;
         this.ticksRemaining = ticks;
      }

      public void decrementTicks() {
         --this.ticksRemaining;
      }

      public boolean isReady() {
         return this.ticksRemaining <= 0;
      }

      public void run() {
         this.action.run();
      }
   }
}
