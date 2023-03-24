package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.Skill.Companion.FISHING
import dev.dakoda.dmo.skills.exp.AbstractChecker.ChecksItems
import dev.dakoda.dmo.skills.exp.AbstractFishingChecker.FishingParams
import dev.dakoda.dmo.skills.exp.data.EXPGain
import dev.dakoda.dmo.skills.exp.data.EXPGain.Provider
import dev.dakoda.dmo.skills.exp.data.EXPGain.Provider.Callback
import dev.dakoda.dmo.skills.exp.data.EXPGain.Rules
import dev.dakoda.dmo.skills.exp.map.EXPMap
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings.Order
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings.Order.DONT_CARE
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

abstract class AbstractFishingChecker : ChecksItems<FishingParams, Rules>() {

    class FishingParams(
        val item: Item
    ) : Provider.Params()

    final override val registry = EXPMap.items<Rules>()

    override fun haveEntryFor(key: Item, order: Order): Boolean {
        return registry.contains(key.defaultStack, order)
    }

    override fun getEntry(key: Item, order: Order): EXPMap.Entry<Rules>? {
        return registry.get(key.defaultStack, order)
    }

    override fun resolve(params: FishingParams, order: Order): EXPGain? {
        if (!haveEntryFor(params.item, order)) return EXPGain(4 to FISHING)

        val provider = getEntry(params.item, order)!!.expGainProvider
        if (provider is FishingProvider) provider.supply(params)

        return provider.resolveEXP()
    }

    protected class FishingProvider(
        private val callback: (ItemStack) -> EXPGain?
    ) : Provider(), Callback<FishingParams> {

        private lateinit var params: FishingParams

        override fun supply(params: FishingParams) {
            this.params = params
        }

        override fun resolveEXP(): EXPGain? {
            return callback(params.item.defaultStack)
        }
    }

    protected fun flat(
        gain: Pair<Int, Skill.Sub>,
        rules: Rules = Rules(),
        settings: Settings = Settings(DONT_CARE)
    ) = EXPMap.Entry(Provider.Default(EXPGain(gain)), rules, settings)

    protected fun callback(
        rules: Rules = Rules(),
        settings: Settings = Settings(DONT_CARE),
        callback: (ItemStack) -> Pair<Int, Skill.Sub>?
    ) = EXPMap.Entry(FishingProvider { i -> callback(i)?.expGain }, rules, settings)

    protected fun settings() = Settings(DONT_CARE)
}
