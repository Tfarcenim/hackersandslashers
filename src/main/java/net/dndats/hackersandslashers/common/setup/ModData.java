package net.dndats.hackersandslashers.common.setup;

import java.util.function.Supplier;
import net.dndats.hackersandslashers.common.data.IsBashingData;
import net.dndats.hackersandslashers.common.data.IsParryingData;
import net.dndats.hackersandslashers.common.data.MobData;
import net.dndats.hackersandslashers.common.data.VisibilityLevelData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;

public class ModData {
   public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES;
   public static final Supplier<AttachmentType<IsParryingData>> IS_PARRYING;
   public static final Supplier<AttachmentType<VisibilityLevelData>> VISIBILITY_LEVEL;
   public static final Supplier<AttachmentType<IsBashingData>> IS_BASHING;
   public static final Supplier<AttachmentType<MobData>> MOB_DETECTABILITY;

   static {
      ATTACHMENT_TYPES = DeferredRegister.create(Keys.ATTACHMENT_TYPES, "hackersandslashers");
      IS_PARRYING = ATTACHMENT_TYPES.register("is_parrying", () -> {
         return AttachmentType.serializable(IsParryingData::new).build();
      });
      VISIBILITY_LEVEL = ATTACHMENT_TYPES.register("visibility_level", () -> {
         return AttachmentType.serializable(VisibilityLevelData::new).build();
      });
      IS_BASHING = ATTACHMENT_TYPES.register("is_bashing", () -> {
         return AttachmentType.serializable(IsBashingData::new).build();
      });
      MOB_DETECTABILITY = ATTACHMENT_TYPES.register("mob_detectability", () -> {
         return AttachmentType.serializable(MobData::new).build();
      });
   }
}
