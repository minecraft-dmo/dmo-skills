package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.DMOSkills.CONFIG
import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.Skill.Companion.CULTIVATION
import dev.dakoda.dmo.skills.Skill.Companion.DUNGEONEER
import dev.dakoda.dmo.skills.Skill.Companion.FORAGING
import dev.dakoda.dmo.skills.Skill.Companion.LUMBERING
import dev.dakoda.dmo.skills.Skill.Companion.MINING
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
        val mining = CONFIG.exp.mining.sources.break_
        registry[BlockTags.COAL_ORES] = flat(mining.coalOres to MINING, rules(false, listOf(PICKAXES)))
        registry[BlockTags.COPPER_ORES] = flat(mining.copperOres to MINING, rules(false, listOf(PICKAXES)))
        registry[BlockTags.IRON_ORES] = flat(mining.ironOres to MINING, rules(false, listOf(PICKAXES)))
        registry[BlockTags.LAPIS_ORES] = flat(mining.lapisOres to MINING, rules(false, listOf(PICKAXES)))
        registry[BlockTags.GOLD_ORES] = flat(mining.goldOres to MINING, rules(false, listOf(PICKAXES)))
        registry[Blocks.NETHER_GOLD_ORE] = flat(mining.netherGoldOre to MINING, rules(false, listOf(PICKAXES)))
        registry[BlockTags.DIAMOND_ORES] = flat(mining.diamondOres to MINING, rules(false, listOf(PICKAXES)))
        registry[BlockTags.EMERALD_ORES] = flat(mining.emeraldOres to MINING, rules(false, listOf(PICKAXES)))
        registry[Blocks.ANCIENT_DEBRIS] = flat(mining.ancientDebris to MINING, rules(false, listOf(PICKAXES)))
        registry[Blocks.AMETHYST_CLUSTER] = flat(mining.amethystCluster to MINING, rules(false, listOf(PICKAXES)))

        // -- Lumbering --
        val lumbering = CONFIG.exp.lumbering.sources.break_
        registry[BlockTags.LOGS] = flat(lumbering.logs to LUMBERING, rules(handTags = listOf(AXES)))
        registry[Blocks.RED_MUSHROOM_BLOCK] = flat(lumbering.mushroomBlocks to LUMBERING, rules(false, listOf(AXES)))
        registry[Blocks.BROWN_MUSHROOM_BLOCK] = flat(lumbering.mushroomBlocks to LUMBERING, rules(false, listOf(AXES)))

        // -- Cultivation --
        val cultivation = CONFIG.exp.cultivation.sources.break_
        registry[BlockTags.CROPS] = callback { _, _, state, _ ->
            when (state.block) {
                is CropBlock -> {
                    if ((state.block as CropBlock).isMature(state)) {
                        cultivation.crops to CULTIVATION
                    } else {
                        null
                    }
                }
                is NetherWartBlock -> {
                    when (state.get(Properties.AGE_3)) {
                        3 -> cultivation.netherWart to CULTIVATION
                        else -> null
                    }
                }
                !is StemBlock -> cultivation.cropsMisc to CULTIVATION
                else -> null
            }
        }
        registry[Blocks.PUMPKIN] = callback(settings = settings(order = BEFORE)) { world, pos, _, _ ->
            val nearby = pos.horizontals(world)
            nearby.filter { it.block is AttachedStemBlock }.forEach {
                try {
                    val facing = it.get(Properties.HORIZONTAL_FACING)
                    val attachedStemBlock = world.getBlockState(pos.add(facing.vector.multiply(-1)))
                    if (attachedStemBlock == it) return@callback cultivation.pumpkin to CULTIVATION
                } catch (e: IllegalArgumentException) {
                    return@forEach
                }
            }
            null
        }
        registry[Blocks.MELON] = flat(cultivation.melon to CULTIVATION, rules(false))

        // -- Foraging --
        val foraging = CONFIG.exp.foraging.sources.harvest
        registry[Blocks.SWEET_BERRY_BUSH] = callback { _, _, state, _ ->
            when (state.get(Properties.AGE_3)) {
                2 -> foraging.sweetBerriesFew to FORAGING
                3 -> foraging.sweetBerriesMany to FORAGING
                else -> null
            }
        }
        registry[BlockTags.CAVE_VINES] = callback { _, _, state, _ ->
            try {
                state.get(Properties.BERRIES)
                foraging.glowBerries to FORAGING
            } catch (e: IllegalArgumentException) {
                null
            }
        }

        // -- Dungeoneer --
        val dungeoneer = CONFIG.exp.dungeoneer.sources.break_
        val lootableContainerCallback: (World, BlockPos, BlockState, BlockEntity?) -> Pair<Int, Skill.Sub>? = lambda@{ _: World, _: BlockPos, _: BlockState, entity: BlockEntity? ->
            if (entity != null && entity is ChestBlockEntity) {
                val chestBlockEntity = entity as LootableContainerBlockEntityAccessor
                if (chestBlockEntity.lootTableId != null) {
                    return@lambda dungeoneer.chest to DUNGEONEER
                }
            }
            return@lambda null
        }
        registry[Blocks.CHEST] = callback(settings = settings(order = BEFORE), callback = lootableContainerCallback)
        registry[Blocks.BARREL] = callback(settings = settings(order = BEFORE), callback = lootableContainerCallback)
        registry[Blocks.TRAPPED_CHEST] = callback(settings = settings(order = BEFORE), callback = lootableContainerCallback)
    }

    private fun BlockPos.horizontals(world: World) = listOf(north(), east(), south(), west()).map {
        world.getBlockState(it)
    }
}
