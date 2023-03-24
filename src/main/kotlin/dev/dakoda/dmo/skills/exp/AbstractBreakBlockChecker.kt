package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.DMOSkills.hasSilkTouch
import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.exp.AbstractBreakBlockChecker.BreakBlockParams
import dev.dakoda.dmo.skills.exp.AbstractBreakBlockChecker.BreakBlockRules
import dev.dakoda.dmo.skills.exp.AbstractChecker.ChecksBlocks
import dev.dakoda.dmo.skills.exp.data.EXPGain
import dev.dakoda.dmo.skills.exp.data.EXPGain.Provider
import dev.dakoda.dmo.skills.exp.data.EXPGain.Provider.Callback
import dev.dakoda.dmo.skills.exp.data.EXPGain.Provider.Default
import dev.dakoda.dmo.skills.exp.data.EXPGain.Provider.Params
import dev.dakoda.dmo.skills.exp.map.EXPMap
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings.Order.AFTER
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class AbstractBreakBlockChecker : ChecksBlocks<BreakBlockParams, BreakBlockRules>() {

    class BreakBlockParams(
        val world: World,
        val hand: ItemStack,
        val blockPos: BlockPos,
        val blockState: BlockState,
        val blockEntity: BlockEntity?
    ) : Params()

    final override val registry = EXPMap.blocks<BreakBlockRules>()

    override fun haveEntryFor(key: Block, order: Settings.Order): Boolean {
        return registry.contains(key.defaultState, order)
    }

    override fun getEntry(key: Block, order: Settings.Order): EXPMap.Entry<BreakBlockRules>? {
        return registry.get(key.defaultState, order)
    }

    override fun resolve(params: BreakBlockParams, order: Settings.Order): EXPGain? {
        if (!haveEntryFor(params.blockState.block, order)) return null

        val entry = getEntry(params.blockState.block, order)!!
        return if (isValidGain(params, entry.rules)) {
            val provider = entry.expGainProvider
            if (provider is BreakBlockProvider) provider.supply(params)
            provider.resolveEXP()
        } else {
            null
        }
    }

    private fun isValidGain(params: BreakBlockParams, rules: BreakBlockRules): Boolean {
        if (!rules.allowSilkTouch && params.hand.hasSilkTouch) return false
        if (rules.handTags.isNotEmpty() && rules.handTags.none { params.hand.isIn(it) }) return false
        return true
    }

    protected class BreakBlockProvider(
        private val callback: (World, BlockPos, BlockState, BlockEntity?) -> EXPGain?
    ) : Provider(), Callback<BreakBlockParams> {

        private lateinit var params: BreakBlockParams

        override fun supply(params: BreakBlockParams) {
            this.params = params
        }

        override fun resolveEXP(): EXPGain? {
            with(params) {
                return callback(world, blockPos, blockState, blockEntity)
            }
        }
    }

    class BreakBlockRules(
        val allowSilkTouch: Boolean = true,
        val handTags: List<TagKey<Item>> = listOf()
    ) : EXPGain.Rules()

    protected fun rules(
        allowSilkTouch: Boolean = true,
        handTags: List<TagKey<Item>> = listOf()
    ) = BreakBlockRules(allowSilkTouch, handTags)

    protected fun flat(
        gain: Pair<Int, Skill.Sub>,
        rules: BreakBlockRules = BreakBlockRules(),
        settings: Settings = Settings(AFTER)
    ) = EXPMap.Entry(Default(EXPGain(gain)), rules, settings)

    protected fun callback(
        rules: BreakBlockRules = BreakBlockRules(),
        settings: Settings = Settings(AFTER),
        callback: (World, BlockPos, BlockState, BlockEntity?) -> Pair<Int, Skill.Sub>?
    ) = EXPMap.Entry(BreakBlockProvider { w, p, s, e -> callback(w, p, s, e)?.expGain }, rules, settings)

    protected fun settings(order: Settings.Order) = Settings(order)
}
