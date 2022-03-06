package dev.dakoda.dmo.skills

import dev.dakoda.dmo.skills.Skill.Sub.LUMBERING
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMP_SKILLS_EXP
import dev.dakoda.dmo.skills.event.PlayerGainEXPCallback
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.tag.BlockTags
import net.minecraft.util.ActionResult

class DMOSkills : ModInitializer {

    override fun onInitialize() {
        PlayerBlockBreakEvents.AFTER.register { world, player: PlayerEntity, blockPos, blockState, blockEntity ->
            if (player is ServerPlayerEntity) {
                val playerSkills = COMP_SKILLS_EXP.get(player).skills
                EXPChecker.BlockBreak.determineEXPGain(blockState.block)?.let {
                    val (inc, skill) = it
                    playerSkills.increase(inc, skill)
                    val result = PlayerGainEXPCallback.EVENT.invoker().handle(player, it)
                    if (result == ActionResult.SUCCESS) {
                        COMP_SKILLS_EXP.sync(player)
                    }
                }
            }
        }
    }
}
