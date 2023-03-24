package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.DMOSkills.CONFIG
import dev.dakoda.dmo.skills.Skill.Companion.FORAGING
import dev.dakoda.dmo.skills.Skill.Companion.LUMBERING
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags.AXES
import net.minecraft.block.Blocks
import net.minecraft.block.Blocks.STRIPPED_ACACIA_LOG
import net.minecraft.block.Blocks.STRIPPED_BIRCH_LOG
import net.minecraft.block.Blocks.STRIPPED_DARK_OAK_LOG
import net.minecraft.block.Blocks.STRIPPED_JUNGLE_LOG
import net.minecraft.block.Blocks.STRIPPED_MANGROVE_LOG
import net.minecraft.block.Blocks.STRIPPED_OAK_LOG
import net.minecraft.block.Blocks.STRIPPED_SPRUCE_LOG
import net.minecraft.registry.tag.BlockTags
import net.minecraft.state.property.Properties

object UseBlockChecker : AbstractUseBlockChecker() {
    init {
        // -- Foraging
        val foraging = CONFIG.exp.foraging.sources.harvest
        registry[Blocks.SWEET_BERRY_BUSH] = callback { _, _, state, _ ->
            when (state.get(Properties.AGE_3)) {
                2 -> foraging.sweetBerriesFew to FORAGING
                3 -> foraging.sweetBerriesMany to FORAGING
                else -> null
            }
        }
        // -- Lumbering --
        val lumbering = CONFIG.exp.lumbering.sources.use
        registry[BlockTags.LOGS] = callback(rules = rules(handTags = listOf(AXES))) { item, world, state, pos ->
            if (state.block !in strippedLogs) lumbering.stripLogs to LUMBERING else null
        }
    }

    private val strippedLogs = listOf(
        STRIPPED_OAK_LOG,
        STRIPPED_SPRUCE_LOG,
        STRIPPED_JUNGLE_LOG,
        STRIPPED_BIRCH_LOG,
        STRIPPED_ACACIA_LOG,
        STRIPPED_DARK_OAK_LOG,
        STRIPPED_MANGROVE_LOG
    )
}
