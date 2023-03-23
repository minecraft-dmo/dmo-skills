package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.SubSkill
import dev.dakoda.dmo.skills.exp.map.EXPMap
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.Item
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class AbstractBreakBlockChecker(
    start: AbstractBreakBlockChecker.() -> Unit,
) : Checker<Block, AbstractBreakBlockChecker.BreakBlockParams, AbstractBreakBlockChecker.BreakBlockRules>() {

    init {
        this.start()
    }

    class BreakBlockParams(
        val blockState: BlockState,
        val blockPos: BlockPos,
        val blockEntity: BlockEntity?,
        val world: World,
    ) : EXPGain.Provider.Params()

    final override val registry = EXPMap.blocks<BreakBlockRules>()

    override fun haveEntryFor(key: Block): Boolean {
        return registry.contains(key.defaultState)
    }

    override fun getEntry(key: Block): EXPMap.Entry<BreakBlockRules>? {
        return registry.get(key.defaultState)
    }

    override fun resolve(params: BreakBlockParams): EXPGain? {
        if (!haveEntryFor(params.blockState.block)) return null

        val provider = getEntry(params.blockState.block)!!.expGainProvider
        if (provider is BreakBlockProvider) provider.supply(params)

        return provider.resolveEXP()
    }

    protected class BreakBlockProvider(
        private val callback: (BlockState, BlockPos, BlockEntity?, World) -> EXPGain?,
    ) : EXPGain.Provider(), EXPGain.Provider.Callback<BreakBlockParams> {

        private lateinit var params: BreakBlockParams

        override fun supply(params: BreakBlockParams) {
            this.params = params
        }

        override fun resolveEXP(): EXPGain? {
            with(params) {
                return callback(blockState, blockPos, blockEntity, world)
            }
        }
    }

    class BreakBlockRules(
        val allowSilkTouch: Boolean = true,
        val handTags: List<TagKey<Item>> = listOf(),
    ) : EXPGain.Rules()

    protected fun rules(
        allowSilkTouch: Boolean = true,
        handTags: List<TagKey<Item>> = listOf()
    ) = BreakBlockRules(allowSilkTouch, handTags)

    protected fun flat(
        gain: Pair<Int, SubSkill>,
        rules: BreakBlockRules = BreakBlockRules(),
        settings: Settings = Settings()
    ) = EXPMap.Entry<BreakBlockRules>(EXPGain.Provider.Default(EXPGain(gain)), rules, settings)

    protected fun callback(
        rules: BreakBlockRules = BreakBlockRules(),
        settings: Settings = Settings(),
        callback: (BlockState, BlockPos, BlockEntity?, World) -> Pair<Int, SubSkill>?,
    ) = EXPMap.Entry<BreakBlockRules>(BreakBlockProvider { s, p, e, w -> callback(s, p, e, w)?.expGain }, rules, settings)
}