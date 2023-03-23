package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.SubSkill
import dev.dakoda.dmo.skills.exp.AbstractCookingChecker.CookingParams
import dev.dakoda.dmo.skills.exp.map.EXPMap
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

abstract class AbstractCookingChecker(
    start: AbstractCookingChecker.() -> Unit,
) : Checker<Item, CookingParams, EXPGain.Rules>() {

    class CookingParams(
        val item: Item,
    ) : EXPGain.Provider.Params()

    final override val registry = EXPMap.items<EXPGain.Rules>()

    init {
        this.start()
    }

    override fun haveEntryFor(key: Item): Boolean {
        return registry.contains(key.defaultStack)
    }

    override fun getEntry(key: Item): EXPMap.Entry<EXPGain.Rules>? {
        return registry.get(key.defaultStack)
    }

    override fun resolve(params: CookingParams): EXPGain? {
        if (!haveEntryFor(params.item)) return null

        val provider = getEntry(params.item)!!.expGainProvider
        if (provider is CookingProvider) provider.supply(params)

        return provider.resolveEXP()
    }

    protected class CookingProvider(
        private val callback: (ItemStack) -> EXPGain,
    ) : EXPGain.Provider(), EXPGain.Provider.Callback<CookingParams> {

        private lateinit var params: CookingParams

        override fun supply(params: CookingParams) {
            this.params = params
        }

        override fun resolveEXP(): EXPGain {
            return callback(params.item.defaultStack)
        }
    }

    protected fun flat(
        gain: Pair<Int, SubSkill>,
        requirements: EXPGain.Rules = EXPGain.Rules(),
        settings: Settings = Settings()
    ) = EXPMap.Entry<EXPGain.Rules>(EXPGain.Provider.Default(EXPGain(gain)), requirements, settings)

    protected fun callback(
        requirements: EXPGain.Rules = EXPGain.Rules(),
        settings: Settings = Settings(),
        callback: (ItemStack) -> Pair<Int, SubSkill>,
    ) = EXPMap.Entry<EXPGain.Rules>(CookingProvider { i -> callback(i).expGain }, requirements, settings)
}