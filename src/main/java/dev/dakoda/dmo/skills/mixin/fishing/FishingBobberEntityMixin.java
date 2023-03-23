package dev.dakoda.dmo.skills.mixin.fishing;

import dev.dakoda.dmo.skills.ModHelper;
import dev.dakoda.dmo.skills.SubSkill;
import dev.dakoda.dmo.skills.exp.FishingEXPCheckerOld;
import kotlin.Pair;
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
        Pair<Integer, SubSkill> gain = FishingEXPCheckerOld.INSTANCE.determineEXPGain(itemStack);
        ModHelper.INSTANCE.gainEXP(playerEntity, gain);
    }
}
