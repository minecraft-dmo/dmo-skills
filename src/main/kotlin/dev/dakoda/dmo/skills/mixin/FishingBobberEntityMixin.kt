package dev.dakoda.dmo.skills.mixin

import dev.dakoda.dmo.skills.EXPChecker
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMP_SKILLS_EXP
import dev.dakoda.dmo.skills.event.PlayerGainEXPCallback
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.FishingBobberEntity
import net.minecraft.item.ItemStack
import net.minecraft.loot.LootTable
import net.minecraft.loot.context.LootContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.At.Shift.AFTER
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import org.spongepowered.asm.mixin.injection.callback.LocalCapture.CAPTURE_FAILHARD

@Mixin(FishingBobberEntity::class)
class FishingBobberEntityMixin {

    @Inject(
        method = ["use(Lnet/minecraft/item/ItemStack;)I"],
        at = [
            At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z",
                shift = AFTER,
                ordinal = 0
            )
        ],
        locals = CAPTURE_FAILHARD
    )
    private fun gainFishingEXP(
        usedItem: ItemStack,
        callbackInfo: CallbackInfoReturnable<Int>,
        playerEntity: PlayerEntity,
        i: Int,
        builder: LootContext.Builder,
        lootTable: LootTable,
        list: List<*>,
        var7: Iterator<*>,
        itemStack: ItemStack,
        itemEntity: ItemEntity,
        d: Double,
        e: Double,
        f: Double,
        g: Double
    ) {
        val playerSkills = COMP_SKILLS_EXP.get(playerEntity).skills
        EXPChecker.Fishing.determineEXPGain(itemStack).let {
            val (inc, skill) = it
            playerSkills.increase(inc, skill)
            val result = PlayerGainEXPCallback.EVENT.invoker().handle(playerEntity as ServerPlayerEntity, it)
            if (result == ActionResult.SUCCESS) {
                COMP_SKILLS_EXP.sync(playerEntity)
            }
        }
    }
}
