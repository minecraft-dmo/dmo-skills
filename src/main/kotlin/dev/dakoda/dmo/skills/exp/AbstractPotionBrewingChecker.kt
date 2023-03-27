package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.exp.AbstractPotionBrewingChecker.BrewingParams
import dev.dakoda.dmo.skills.exp.data.EXPGain
import dev.dakoda.dmo.skills.exp.data.EXPGain.Provider
import dev.dakoda.dmo.skills.exp.data.EXPGain.Provider.Callback
import dev.dakoda.dmo.skills.exp.data.EXPGain.Provider.Params
import dev.dakoda.dmo.skills.exp.data.EXPGain.Rules
import dev.dakoda.dmo.skills.exp.map.EXPMap
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings.Order.DONT_CARE
import net.minecraft.potion.Potion

abstract class AbstractPotionBrewingChecker : AbstractChecker.ChecksPotions<BrewingParams, Rules>() {

    class BrewingParams(
        val potion: Potion
    ) : Params()

    final override val registry = EXPMap.potions<Rules>()

    override fun haveEntryFor(key: Potion, order: Settings.Order): Boolean {
        return registry.contains(key, order)
    }

    override fun getEntry(key: Potion, order: Settings.Order): EXPMap.Entry<Rules>? {
        return registry.get(key, order)
    }

    override fun resolve(params: BrewingParams, order: Settings.Order): EXPGain? {
        if (!haveEntryFor(params.potion, order)) return null

        val provider = getEntry(params.potion, order)!!.expGainProvider
        if (provider is BrewingProvider) provider.supply(params)

        return provider.resolveEXP()
    }

    protected class BrewingProvider(
        private val callback: (Potion) -> EXPGain?
    ) : Provider(), Callback<BrewingParams> {

        private lateinit var params: BrewingParams

        override fun supply(params: BrewingParams) {
            this.params = params
        }

        override fun resolveEXP(): EXPGain? {
            return callback(params.potion)
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
        callback: (Potion) -> Pair<Int, Skill.Sub>?
    ) = EXPMap.Entry(BrewingProvider { i -> callback(i)?.expGain }, rules, settings)

    protected fun settings() = Settings(DONT_CARE)
}
