package dev.dakoda.dmo.skills.mixin.dungeoneer;

import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootableContainerBlockEntity.class)
public interface LootableContainerBlockEntityAccessor {

    @Accessor(value = "lootTableId")
    Identifier getLootTableId();
}
