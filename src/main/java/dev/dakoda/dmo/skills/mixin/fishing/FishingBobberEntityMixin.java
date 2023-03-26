package dev.dakoda.dmo.skills.mixin.fishing;

import dev.dakoda.dmo.skills.DMOSkills;
import dev.dakoda.dmo.skills.exp.AbstractFishingChecker.FishingParams;
import dev.dakoda.dmo.skills.exp.FishingChecker;
import dev.dakoda.dmo.skills.exp.data.EXPGain;
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings.Order;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {

    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z",
                    shift = At.Shift.AFTER,
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    void mixin_use(
            ItemStack usedItem, CallbackInfoReturnable<Integer> cir,
            PlayerEntity playerEntity, int i,
            LootContext.Builder builder, LootTable lootTable,
            List<Object> list, Iterator<Object> iterator,
            ItemStack itemStack
    ) {
        if (!playerEntity.isCreative() && !playerEntity.isSpectator()) {
            EXPGain gain = FishingChecker.INSTANCE.resolve(new FishingParams(itemStack.getItem()), Order.DONT_CARE);
            if (gain != null) DMOSkills.INSTANCE.gainEXP(playerEntity, gain);
        }
    }
}
