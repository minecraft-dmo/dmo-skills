package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.ModHelper.horizontals
import dev.dakoda.dmo.skills.Skill.Companion.CULTIVATION
import dev.dakoda.dmo.skills.Skill.Companion.LUMBERING
import dev.dakoda.dmo.skills.Skill.Companion.MINING
import dev.dakoda.dmo.skills.exp.BreakBlockChecker.callback
import dev.dakoda.dmo.skills.exp.BreakBlockChecker.flat
import dev.dakoda.dmo.skills.exp.BreakBlockChecker.rules
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags.AXES
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags.PICKAXES
import net.minecraft.block.AttachedStemBlock
import net.minecraft.block.Blocks
import net.minecraft.block.CropBlock
import net.minecraft.block.NetherWartBlock
import net.minecraft.block.StemBlock
import net.minecraft.registry.tag.BlockTags
import net.minecraft.state.property.Properties
import java.lang.IllegalArgumentException

object BreakBlockChecker : AbstractBreakBlockChecker({
    // -- Mining --
    registry[BlockTags.COAL_ORES] = flat(1 to MINING, rules(false, listOf(PICKAXES)))
    registry[BlockTags.COPPER_ORES] = flat(2 to MINING, rules(false, listOf(PICKAXES)))
    registry[BlockTags.IRON_ORES] = flat(3 to MINING, rules(false, listOf(PICKAXES)))
    registry[BlockTags.LAPIS_ORES] = flat(5 to MINING, rules(false, listOf(PICKAXES)))
    registry[BlockTags.GOLD_ORES] = flat(5 to MINING, rules(false, listOf(PICKAXES)))
    registry[Blocks.NETHER_GOLD_ORE] = flat(1 to MINING, rules(false, listOf(PICKAXES)))
    registry[BlockTags.DIAMOND_ORES] = flat(8 to MINING, rules(false, listOf(PICKAXES)))
    registry[BlockTags.EMERALD_ORES] = flat(12 to MINING, rules(false, listOf(PICKAXES)))
    registry[Blocks.ANCIENT_DEBRIS] = flat(12 to MINING, rules(false, listOf(PICKAXES)))
    registry[Blocks.AMETHYST_CLUSTER] = flat(7 to MINING, rules(false, listOf(PICKAXES)))

    // -- Lumbering --
    registry[BlockTags.LOGS] = flat(2 to LUMBERING, rules(handTags = listOf(AXES)))
    registry[Blocks.RED_MUSHROOM_BLOCK] = flat(2 to LUMBERING, rules(false, listOf(AXES)))
    registry[Blocks.BROWN_MUSHROOM_BLOCK] = flat(2 to LUMBERING, rules(false, listOf(AXES)))

    // -- Cultivation --
    registry[BlockTags.CROPS] = callback { state, _, _, _ ->
        when(state.block) {
            is CropBlock -> {
                if ((state.block as CropBlock).isMature(state)) 6 to CULTIVATION
                else null
            }
            is NetherWartBlock -> {
                when (state.get(Properties.AGE_3)) {
                    3 -> 6 to CULTIVATION
                    else -> null
                }
            }
            !is StemBlock -> 1 to CULTIVATION
            else -> null
        }
    }
    registry[Blocks.PUMPKIN] = callback { _, pos, _, world ->
        val nearby = pos.horizontals(world)
        nearby.filter { it.block is AttachedStemBlock }.forEach {
            try {
                val facing = it.get(Properties.HORIZONTAL_FACING)
                val attachedStemBlock = world.getBlockState(pos.add(facing.vector.multiply(-1)))
                if (attachedStemBlock == it) return@callback 6 to CULTIVATION
            } catch (e: IllegalArgumentException) {
                return@forEach
            }
        }
        null
    }
})