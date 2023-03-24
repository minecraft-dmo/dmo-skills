package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.exp.AbstractChecker.ChecksBlocks
import dev.dakoda.dmo.skills.exp.AbstractUseBlockChecker.UseBlockParams
import dev.dakoda.dmo.skills.exp.AbstractUseBlockChecker.UseBlockRules
import dev.dakoda.dmo.skills.exp.data.EXPGain
import dev.dakoda.dmo.skills.exp.data.EXPGain.Provider
import dev.dakoda.dmo.skills.exp.data.EXPGain.Provider.Callback
import dev.dakoda.dmo.skills.exp.data.EXPGain.Provider.Default
import dev.dakoda.dmo.skills.exp.data.EXPGain.Provider.Params
import dev.dakoda.dmo.skills.exp.map.EXPMap
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings.Order.DONT_CARE
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class AbstractUseBlockChecker : ChecksBlocks<UseBlockParams, UseBlockRules>() {

    class UseBlockParams(
        val hand: ItemStack,
        val world: World,
        val blockState: BlockState,
        val blockPos: BlockPos
    ) : Params()

    final override val registry = EXPMap.blocks<UseBlockRules>()

    override fun haveEntryFor(key: Block, order: Settings.Order): Boolean {
        return registry.contains(key.defaultState, order)
    }

    override fun getEntry(key: Block, order: Settings.Order): EXPMap.Entry<UseBlockRules>? {
        return registry.get(key.defaultState, order)
    }

    override fun resolve(params: UseBlockParams, order: Settings.Order): EXPGain? {
        if (!haveEntryFor(params.blockState.block, order)) return null

        val entry = getEntry(params.blockState.block, order)!!
        return if (isValidGain(params, entry.rules)) {
            val provider = entry.expGainProvider
            if (provider is UseBlockProvider) provider.supply(params)
            provider.resolveEXP()
        } else {
            null
        }
    }

    private fun isValidGain(params: UseBlockParams, rules: UseBlockRules): Boolean {
        if (rules.handTags.isNotEmpty() && rules.handTags.none { params.hand.isIn(it) }) return false
        return true
    }

    protected class UseBlockProvider(
        private val callback: (ItemStack, World, BlockState, BlockPos) -> EXPGain?
    ) : Provider(), Callback<UseBlockParams> {

        private lateinit var params: UseBlockParams

        override fun supply(params: UseBlockParams) {
            this.params = params
        }

        override fun resolveEXP(): EXPGain? {
            with(params) {
                return callback(hand, world, blockState, blockPos)
            }
        }
    }

    class UseBlockRules(
        val handTags: List<TagKey<Item>> = listOf()
    ) : EXPGain.Rules()

    protected fun rules(
        handTags: List<TagKey<Item>> = listOf()
    ) = UseBlockRules(handTags)

    protected fun flat(
        gain: Pair<Int, Skill.Sub>,
        rules: UseBlockRules = UseBlockRules(),
        settings: Settings = Settings(DONT_CARE)
    ) = EXPMap.Entry(Default(EXPGain(gain)), rules, settings)

    protected fun callback(
        rules: UseBlockRules = UseBlockRules(),
        settings: Settings = Settings(DONT_CARE),
        callback: (ItemStack, World, BlockState, BlockPos) -> Pair<Int, Skill.Sub>?
    ) = EXPMap.Entry(UseBlockProvider { h, w, s, p -> callback(h, w, s, p)?.expGain }, rules, settings)

    protected fun settings() = Settings(DONT_CARE)
}
