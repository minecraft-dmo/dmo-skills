package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.ModHelper.CONFIG
import dev.dakoda.dmo.skills.ModHelper.hasSilkTouch
import dev.dakoda.dmo.skills.ModHelper.horizontals
import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.Skill.Companion.CULTIVATION
import dev.dakoda.dmo.skills.Skill.Companion.FORAGING
import dev.dakoda.dmo.skills.Skill.Companion.LUMBERING
import dev.dakoda.dmo.skills.Skill.Companion.MINING
import dev.dakoda.dmo.skills.SubSkill
import dev.dakoda.dmo.skills.exp.BreakBlockEXPCheckerOld.Settings.Order.BEFORE
import dev.dakoda.dmo.skills.mixin.dungeoneer.LootableContainerBlockEntityAccessor
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags.AXES
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags.PICKAXES
import net.minecraft.block.AttachedStemBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.CropBlock
import net.minecraft.block.NetherWartBlock
import net.minecraft.block.StemBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.property.Properties
import net.minecraft.state.property.Properties.HORIZONTAL_FACING
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.lang.IllegalArgumentException

private typealias BreakBlockCallback = (BlockState, BlockPos, World, BlockEntity?) -> Pair<Int, SubSkill>?

object BreakBlockEXPCheckerOld : EXPCheckerOld {

    data class Settings(
        var allowSilkTouch: Boolean = false,
        var handTags: List<TagKey<Item>> = listOf(),
        var order: Order = Order.AFTER,
    ) {
        enum class Order {
            BEFORE, AFTER
        }
    }

    fun settings(func: Settings.() -> Unit) = Settings().apply(func)

    private val expGains: MutableMap<Block, Pair<Pair<Int, SubSkill>, Settings>> = mutableMapOf()
    private val expGainsTags: MutableMap<TagKey<Block>, Pair<Pair<Int, SubSkill>, Settings>> = mutableMapOf()
    private val callbacks: MutableMap<Block, Pair<BreakBlockCallback, Settings>> = mutableMapOf()
    private val callbacksTags: MutableMap<TagKey<Block>, Pair<BreakBlockCallback, Settings>> = mutableMapOf()

    private fun gains(order: Settings.Order) = expGains.filter { it.value.second.order == order }
    private fun gainsTags(order: Settings.Order) = expGainsTags.filter { it.value.second.order == order }
    private fun callbacks(order: Settings.Order) = callbacks.filter { it.value.second.order == order }
    private fun callbacksTags(order: Settings.Order) = callbacksTags.filter { it.value.second.order == order }

    init {
        // -- Mining --
        register(tag = BlockTags.COAL_ORES, expGain = 1 to MINING, settings { allowSilkTouch = false; handTags = listOf(PICKAXES) })
        register(tag = BlockTags.COPPER_ORES, expGain = 2 to MINING, settings { allowSilkTouch = false; handTags = listOf(PICKAXES) })
        register(tag = BlockTags.IRON_ORES, expGain = 3 to MINING, settings { allowSilkTouch = false; handTags = listOf(PICKAXES) })
        register(tag = BlockTags.LAPIS_ORES, expGain = 5 to MINING, settings { allowSilkTouch = false; handTags = listOf(PICKAXES) })
        register(tag = BlockTags.GOLD_ORES, expGain = 5 to MINING, settings { allowSilkTouch = false; handTags = listOf(PICKAXES) })
        register(block = Blocks.NETHER_GOLD_ORE, expGain = 1 to MINING, settings { allowSilkTouch = false; handTags = listOf(PICKAXES) })
        register(tag = BlockTags.DIAMOND_ORES, expGain = 8 to MINING, settings { allowSilkTouch = false; handTags = listOf(PICKAXES) })
        register(tag = BlockTags.EMERALD_ORES, expGain = 12 to MINING, settings { allowSilkTouch = false; handTags = listOf(PICKAXES) })
        register(block = Blocks.ANCIENT_DEBRIS, expGain = 12 to MINING, settings { allowSilkTouch = false; handTags = listOf(PICKAXES) })
        register(block = Blocks.AMETHYST_CLUSTER, expGain = 7 to MINING, settings { allowSilkTouch = false; handTags = listOf(PICKAXES) })

        // -- Lumbering --
        register(tag = BlockTags.LOGS, expGain = 2 to LUMBERING, settings { handTags = listOf(AXES) })
        register(block = Blocks.RED_MUSHROOM_BLOCK, expGain = 2 to LUMBERING, settings { handTags = listOf(AXES) })
        register(block = Blocks.BROWN_MUSHROOM_BLOCK, expGain = 2 to LUMBERING, settings { handTags = listOf(AXES) })

        // -- Cultivation --
        register(tag = BlockTags.CROPS) { blockState, _, _, _ ->
            when (blockState.block) {
                is CropBlock -> {
                    if ((blockState.block as CropBlock).isMature(blockState)) {
                        6 to CULTIVATION
                    } else null
                }
                is NetherWartBlock -> {
                    when (blockState.get(Properties.AGE_3)) {
                        3 -> 6 to CULTIVATION
                        else -> null
                    }
                }
                !is StemBlock -> 1 to CULTIVATION
                else -> null
            }
        }
        register(block = Blocks.PUMPKIN, settings { order = BEFORE }) { _, pos, world, _ ->
            val nearbyBlocks = pos.horizontals(world)
            if (nearbyBlocks.any { it.block is AttachedStemBlock }) {
                nearbyBlocks.filter { it.block is AttachedStemBlock }.forEach {
                    try {
                        val facing = it.get(HORIZONTAL_FACING)
                        val attachedStemBlock = world.getBlockState(pos.add(facing.vector.multiply(-1)))
                        if (attachedStemBlock == it) return@register 6 to CULTIVATION
                    } catch (e: IllegalArgumentException) {
                        return@forEach
                    }
                }
            }
            null
        }
        register(block = Blocks.MELON, expGain = 6 to CULTIVATION, settings { allowSilkTouch = false })

        // -- Foraging --
        register(block = Blocks.SWEET_BERRY_BUSH) { blockState, _, _, _ ->
            when(blockState.get(Properties.AGE_3)) {
                2 -> 3 to FORAGING
                3 -> 5 to FORAGING
                else -> null
            }
        }
        register(tag = BlockTags.CAVE_VINES) { blockState, _, _, _ ->
            if (blockState.get(Properties.BERRIES)) 5 to FORAGING else null
        }

        // -- Dungeoneer --
        val lootableContainerCallback: BreakBlockCallback = { _: BlockState, _: BlockPos, _: World, blockEntity: BlockEntity? ->
            if (blockEntity != null && blockEntity is ChestBlockEntity) {
                val chestBlockEntity = (blockEntity as LootableContainerBlockEntityAccessor)
                if (chestBlockEntity.lootTableId != null) {
                    CONFIG.exp.dungeoneer.sources.break_.chest to Skill.DUNGEONEER
                }
            }
            null
        }
        register(block = Blocks.CHEST, settings { order = BEFORE }, func = lootableContainerCallback)
        register(block = Blocks.TRAPPED_CHEST, settings { order = BEFORE }, func = lootableContainerCallback)
        register(block = Blocks.BARREL, settings { order = BEFORE }, func = lootableContainerCallback)
    }

    fun register(
        block: Block, expGain: Pair<Int, SubSkill>,
        settings: Settings = settings {}
    ) {
        expGains[block] = expGain to settings
    }

    fun register(
        tag: TagKey<Block>, expGain: Pair<Int, SubSkill>,
        settings: Settings = settings {}
    ) {
        expGainsTags[tag] = expGain to settings
    }

    fun register(
        block: Block,
        settings: Settings = settings {},
        func: BreakBlockCallback
    ) {
        callbacks[block] = func to settings
    }

    fun register(
        tag: TagKey<Block>,
        settings: Settings = settings {},
        func: BreakBlockCallback
    ) {
        callbacksTags[tag] = func to settings
    }

    fun doEXPGain(
        blockState: BlockState, blockPos: BlockPos,
        world: World, blockEntity: BlockEntity?, hand: ItemStack,
        order: Settings.Order = Settings.Order.AFTER,
    ): Pair<Int, SubSkill>? {
        if (gains(order).containsKey(blockState.block)) {
            val expGain = gains(order)[blockState.block]?.first
            val settings = gains(order)[blockState.block]?.second!!
            return onlyIfSettingsOkay(expGain, hand, settings)
        } else {
            gainsTags(order).forEach { (tag, pair) ->
                val (expGain, settings) = pair
                if (blockState.isIn(tag)) {
                    return onlyIfSettingsOkay(expGain, hand, settings)
                }
            }
            return doCallbacks(blockState, blockPos, world, blockEntity, hand, order)
        }
    }

    private fun doCallbacks(
        blockState: BlockState, blockPos: BlockPos,
        world: World, blockEntity: BlockEntity?, hand: ItemStack,
        order: Settings.Order = Settings.Order.AFTER,
    ): Pair<Int, SubSkill>? {
        if (callbacks(order).containsKey(blockState.block)) {
            val (callback, settings) = callbacks(order)[blockState.block]!!
            return onlyIfSettingsOkay(callback.invoke(blockState, blockPos, world, blockEntity), hand, settings)
        } else {
            callbacksTags(order).forEach { (tag, pair) ->
                val (callback, settings) = pair
                if (blockState.isIn(tag)) {
                    return onlyIfSettingsOkay(callback.invoke(blockState, blockPos, world, blockEntity), hand, settings)
                }
            }
            return null
        }
    }

    private fun <T : Any?> onlyIfSettingsOkay(t: T, hand: ItemStack, settings: Settings): T? {
        if (!settings.allowSilkTouch && hand.hasSilkTouch) return null
        if (settings.handTags.isNotEmpty() && settings.handTags.none { hand.isIn(it) }) return null
        return t
    }
}