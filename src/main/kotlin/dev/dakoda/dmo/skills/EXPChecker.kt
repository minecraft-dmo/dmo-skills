package dev.dakoda.dmo.skills

import dev.dakoda.dmo.skills.Skill.Sub.LUMBERING
import dev.dakoda.dmo.skills.Skill.Sub.MINING
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.Blocks.NETHER_QUARTZ_ORE
import net.minecraft.tag.BlockTags.COAL_ORES
import net.minecraft.tag.BlockTags.COPPER_ORES
import net.minecraft.tag.BlockTags.DEEPSLATE_ORE_REPLACEABLES
import net.minecraft.tag.BlockTags.DIAMOND_ORES
import net.minecraft.tag.BlockTags.EMERALD_ORES
import net.minecraft.tag.BlockTags.GOLD_ORES
import net.minecraft.tag.BlockTags.IRON_ORES
import net.minecraft.tag.BlockTags.LAPIS_ORES
import net.minecraft.tag.BlockTags.LOGS
import net.minecraft.tag.BlockTags.PICKAXE_MINEABLE
import net.minecraft.tag.BlockTags.REDSTONE_ORES
import net.minecraft.tag.BlockTags.STONE_ORE_REPLACEABLES

object EXPChecker {

    object BlockBreak {

        fun determineEXPGain(block: Block): Pair<Int, Skill.Sub>? {
            return when (block) {
                in PICKAXE_MINEABLE -> when(block) {
                    in STONE_ORE_REPLACEABLES -> 1 to MINING
                    in DEEPSLATE_ORE_REPLACEABLES -> 1 to MINING
                    in COAL_ORES -> 2 to MINING
                    in COPPER_ORES -> 2 to MINING
                    in IRON_ORES -> 3 to MINING
                    in LAPIS_ORES -> 5 to MINING
                    in GOLD_ORES -> 5 to MINING
                    in REDSTONE_ORES -> 5 to MINING
                    in DIAMOND_ORES -> 8 to MINING
                    in EMERALD_ORES -> 12 to MINING
                    else -> {
                        if (block == NETHER_QUARTZ_ORE) 2 to MINING
                        null
                    }
                }
                in LOGS -> 1 to LUMBERING
                else -> null
            }
        }
    }
}
