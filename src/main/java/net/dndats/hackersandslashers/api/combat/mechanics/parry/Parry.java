package net.dndats.hackersandslashers.api.combat.mechanics.parry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.common.data.IsParryingData;
import net.dndats.hackersandslashers.common.network.packets.PacketTriggerPlayerParry;
import net.dndats.hackersandslashers.common.setup.ModAttributes;
import net.dndats.hackersandslashers.common.setup.ModData;
import net.dndats.hackersandslashers.utils.CombatHelper;
import net.dndats.hackersandslashers.utils.EntityHelper;
import net.dndats.hackersandslashers.utils.ItemHelper;
import net.dndats.hackersandslashers.utils.PlayerHelper;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShieldItem;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class Parry {
   private static final Random lostHealthComparator = new Random();
   private static final HashSet<ResourceKey<DamageType>> damageSourcesAccepted;

   public static int getCooldown(Player player) {
      AttributeInstance cooldown = player.getAttribute(ModAttributes.PARRY_RECHARGE);
      return cooldown == null ? 0 : (int)cooldown.getValue();
   }

   public static void parryBehavior(float damageReduction, LivingIncomingDamageEvent event) {
      LivingEntity var3 = event.getEntity();
      if (var3 instanceof Player) {
         Player player = (Player)var3;
         Stream var10000 = damageSourcesAccepted.stream();
         DamageSource var10001 = event.getSource();
         Objects.requireNonNull(var10001);
         if (!var10000.noneMatch(var10001::is)) {
            if (PlayerHelper.isParrying(player)) {
               handleParryEffects(player, event, damageReduction);
            }
         }
      }
   }

   private static void handleParryEffects(Player player, LivingIncomingDamageEvent event, float damageReduction) {
      SoundEffects.playParrySound(player);
      ItemHelper.damageBlockWeapon(player, (int)event.getAmount());
      event.setAmount(calculateReducedDamage(event.getAmount(), damageReduction));
      if (shouldStunAttacker(event)) {
         CombatHelper.stunAttackingEntity(event);
      }

   }

   private static float calculateReducedDamage(float originalDamage, float reductionPercentage) {
      return originalDamage * (reductionPercentage / 100.0F);
   }

   private static boolean shouldStunAttacker(LivingIncomingDamageEvent event) {
      if (event.getSource().isDirect()) {
         Entity var2 = event.getSource().getEntity();
         if (var2 instanceof LivingEntity) {
            LivingEntity source = (LivingEntity)var2;
            int chance = lostHealthComparator.nextInt(100);
            return EntityHelper.getMobLostHealth(source) >= chance;
         }
      }

      return false;
   }

   public static void triggerDefensive(int duration, Player player) {
      if (player != null) {
         if (canParry(player)) {
            IsParryingData playerData = (IsParryingData)player.getData(ModData.IS_PARRYING);
            playerData.setIsParrying(true);
            PacketDistributor.sendToServer(new PacketTriggerPlayerParry(playerData, duration), new CustomPacketPayload[0]);
         }

      }
   }

   public static boolean canParry(Player player) {
      return !player.isCrouching() && !PlayerHelper.isParrying(player) && !PlayerHelper.isPointingAtInteractable(player) && ItemHelper.isHoldingParryItem(player) && !(player.getOffhandItem().getItem() instanceof ShieldItem) && !player.swinging;
   }

   static {
      damageSourcesAccepted = new HashSet(Arrays.asList(DamageTypes.PLAYER_ATTACK, DamageTypes.MOB_ATTACK, DamageTypes.ARROW, DamageTypes.MOB_ATTACK_NO_AGGRO, DamageTypes.EXPLOSION, DamageTypes.MOB_PROJECTILE));
   }
}
