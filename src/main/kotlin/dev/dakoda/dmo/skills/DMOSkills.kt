package dev.dakoda.dmo.skills

import dev.dakoda.dmo.skills.ModHelper.hasSilkTouch
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMP_SKILLS_EXP
import dev.dakoda.dmo.skills.event.PlayerGainEXPCallback
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult.PASS
import net.minecraft.util.ActionResult.SUCCESS

class DMOSkills : ModInitializer {

    override fun onInitialize() {
        PlayerBlockBreakEvents.AFTER.register { world, player, blockPos, blockState, blockEntity ->
            if (player is ServerPlayerEntity) {
                if (player.isSurvival) {
                    val usedSilkTouch = player.mainHandStack.hasSilkTouch
                    val playerSkills = COMP_SKILLS_EXP.get(player).skills
                    EXPChecker.BlockBreak.determineEXPGain(world, blockPos, blockState, usedSilkTouch)?.let {
                        val (inc, skill) = it
                        playerSkills.increase(inc, skill)
                        val result = PlayerGainEXPCallback.EVENT.invoker().handle(player, it)
                        if (result == SUCCESS) {
                            COMP_SKILLS_EXP.sync(player)
                        }
                    }
                }
            }
        }

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register { world, entity, killedEntity ->
            if (entity is ServerPlayerEntity) {
                if (entity.isSurvival) {
                    val playerSkills = COMP_SKILLS_EXP.get(entity).skills
                    EXPChecker.Combat.Kill.determineEXPGain(entity.mainHandStack, killedEntity).forEach {
                        val (inc, skill) = it ?: return@forEach
                        playerSkills.increase(inc, skill)
                        val result = PlayerGainEXPCallback.EVENT.invoker().handle(entity, it)
                        if (result == SUCCESS) {
                            COMP_SKILLS_EXP.sync(entity)
                        }
                    }
                }
            }
        }

        AttackEntityCallback.EVENT.register { player, world, hand, killedEntity, entityHitResult ->
            if (player.isSurvival) {
                val playerSkills = COMP_SKILLS_EXP.get(player).skills
                EXPChecker.Combat.Attack.determineEXPGain(player.mainHandStack, killedEntity).let {
                    val (inc, skill) = it
                    playerSkills.increase(inc, skill)
                    val result = PlayerGainEXPCallback.EVENT.invoker().handle(player as ServerPlayerEntity, it)
                    if (result == SUCCESS) {
                        COMP_SKILLS_EXP.sync(player)
                        return@let SUCCESS
                    }
                    return@let PASS
                }
            } else return@register PASS
        }
    }

    private val PlayerEntity.isSurvival get() = true
}
