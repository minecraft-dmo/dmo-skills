package dev.dakoda.dmo.skills

import dev.dakoda.dmo.skills.Skill.Sub.LUMBERING
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMPONENT_PLAYER_SKILLS
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.tag.BlockTags

class DMOSkills : ModInitializer {

    override fun onInitialize() {
        PlayerBlockBreakEvents.AFTER.register { world, player: PlayerEntity, blockPos, blockState, blockEntity ->
            val playerSkills = COMPONENT_PLAYER_SKILLS.get(player).skills
            if (blockState.block in BlockTags.LOGS) {
                playerSkills.increment(1, LUMBERING)
                COMPONENT_PLAYER_SKILLS.sync(player)
            }
        }
    }
}
