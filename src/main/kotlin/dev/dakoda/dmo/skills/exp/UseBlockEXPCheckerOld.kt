package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.Skill.Companion.LUMBERING
import dev.dakoda.dmo.skills.SubSkill
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags.AXES
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks.STRIPPED_ACACIA_LOG
import net.minecraft.block.Blocks.STRIPPED_BIRCH_LOG
import net.minecraft.block.Blocks.STRIPPED_DARK_OAK_LOG
import net.minecraft.block.Blocks.STRIPPED_JUNGLE_LOG
import net.minecraft.block.Blocks.STRIPPED_MANGROVE_LOG
import net.minecraft.block.Blocks.STRIPPED_OAK_LOG
import net.minecraft.block.Blocks.STRIPPED_SPRUCE_LOG
import net.minecraft.block.Blocks.SWEET_BERRY_BUSH
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

private typealias UseBlockCallback = (BlockState, BlockPos, World) -> Pair<Int, SubSkill>?

object UseBlockEXPCheckerOld : EXPCheckerOld {

    data class Settings(
        var handTags: List<TagKey<Item>> = listOf(),
    )

    fun settings(func: Settings.() -> Unit) = Settings().apply(func)

    private val callbacks: MutableMap<Block, Pair<UseBlockCallback, Settings>> = mutableMapOf()
    private val callbacksTags: MutableMap<TagKey<Block>, Pair<UseBlockCallback, Settings>> = mutableMapOf()

    init {
        register(block = SWEET_BERRY_BUSH) { blockState, _, _ ->
            when (blockState.get(Properties.AGE_3)) {
                2 -> 3 to Skill.FORAGING
                3 -> 5 to Skill.FORAGING
                else -> null
            }
        }
        register(tag = BlockTags.LOGS, settings { handTags = listOf(AXES) }) { blockState, blockPos, world ->
            if (blockState.block !in strippedLogs) {
                5 to LUMBERING
            } else null
        }
    }

    fun register(block: Block, settings: Settings = settings {}, func: UseBlockCallback) {
        callbacks[block] = func to settings
    }

    fun register(tag: TagKey<Block>, settings: Settings = settings {}, func: UseBlockCallback) {
        callbacksTags[tag] = func to settings
    }

    fun doCallback(blockState: BlockState, blockPos: BlockPos, world: World, hand: ItemStack): Pair<Int, SubSkill>? {
        if (callbacks.containsKey(blockState.block)) {
            val (callback, settings) = callbacks[blockState.block]!!
            return onlyIfSettingsOkay(callback.invoke(blockState, blockPos, world), hand, settings)
        } else {
            callbacksTags.forEach { (tag, pair) ->
                val (callback, settings) = pair
                if (blockState.isIn(tag)) {
                    return onlyIfSettingsOkay(callback.invoke(blockState, blockPos, world), hand, settings)
                }
            }
            return null
        }
    }

    private fun <T : Any?> onlyIfSettingsOkay(t: T, hand: ItemStack, settings: Settings): T? {
        if (settings.handTags.none { hand.isIn(it) }) return null
        return t
    }

    private val strippedLogs = listOf(
        STRIPPED_OAK_LOG, STRIPPED_SPRUCE_LOG, STRIPPED_JUNGLE_LOG,
        STRIPPED_BIRCH_LOG, STRIPPED_ACACIA_LOG, STRIPPED_DARK_OAK_LOG,
        STRIPPED_MANGROVE_LOG
    )
}