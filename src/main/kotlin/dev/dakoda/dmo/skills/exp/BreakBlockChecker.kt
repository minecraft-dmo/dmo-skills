package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.ModHelper.CONFIG
import dev.dakoda.dmo.skills.ModHelper.horizontals
import dev.dakoda.dmo.skills.Skill.Companion.CULTIVATION
import dev.dakoda.dmo.skills.Skill.Companion.DUNGEONEER
import dev.dakoda.dmo.skills.Skill.Companion.FORAGING
import dev.dakoda.dmo.skills.Skill.Companion.LUMBERING
import dev.dakoda.dmo.skills.Skill.Companion.MINING
import dev.dakoda.dmo.skills.SubSkill
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings.Order.BEFORE
import dev.dakoda.dmo.skills.mixin.dungeoneer.LootableContainerBlockEntityAccessor
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags.AXES
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags.PICKAXES
import net.minecraft.block.AttachedStemBlock
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.CropBlock
import net.minecraft.block.NetherWartBlock
import net.minecraft.block.StemBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.registry.tag.BlockTags
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object BreakBlockChecker : AbstractBreakBlockChecker() {
    init {
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
        registry[BlockTags.CROPS] = callback { _, _, state, _ ->
            when (state.block) {
                is CropBlock -> {
                    if ((state.block as CropBlock).isMature(state)) {
                        6 to CULTIVATION
                    } else {
                        null
                    }
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
        registry[Blocks.PUMPKIN] = callback(settings = settings(order = BEFORE)) { world, pos, _, _ ->
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
        registry[Blocks.MELON] = flat(6 to CULTIVATION, rules(false))

        // -- Foraging --
        registry[Blocks.SWEET_BERRY_BUSH] = callback { _, _, state, _ ->
            when (state.get(Properties.AGE_3)) {
                2 -> 3 to FORAGING
                3 -> 5 to FORAGING
                else -> null
            }
        }
        registry[BlockTags.CAVE_VINES] = callback { _, _, state, _ ->
            if (state.get(Properties.BERRIES)) 5 to FORAGING else null
        }

        // -- Dungeoneer --
        val lootableContainerCallback: (World, BlockPos, BlockState, BlockEntity?) -> Pair<Int, SubSkill>? = lambda@{ _: World, _: BlockPos, _: BlockState, entity: BlockEntity? ->
            if (entity != null && entity is ChestBlockEntity) {
                val chestBlockEntity = entity as LootableContainerBlockEntityAccessor
                if (chestBlockEntity.lootTableId != null) {
                    return@lambda CONFIG.exp.dungeoneer.sources.break_.chest to DUNGEONEER
                }
            }
            return@lambda null
        }
        registry[Blocks.CHEST] = callback(settings = settings(order = BEFORE), callback = lootableContainerCallback)
        registry[Blocks.BARREL] = callback(settings = settings(order = BEFORE), callback = lootableContainerCallback)
        registry[Blocks.TRAPPED_CHEST] = callback(settings = settings(order = BEFORE), callback = lootableContainerCallback)
    }
}
