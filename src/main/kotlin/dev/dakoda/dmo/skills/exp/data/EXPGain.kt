package dev.dakoda.dmo.skills.exp.data

import dev.dakoda.dmo.skills.SubSkill

data class EXPGain(
    var amount: Int,
    var skill: SubSkill
) {
    constructor(pair: Pair<Int, SubSkill>) : this(pair.first, pair.second)

    abstract class Provider {
        abstract fun resolveEXP(): EXPGain?

        interface Flat
        interface Callback<P : Params> {
            fun supply(params: P)
        }

        class Default(private val expGain: EXPGain) : Provider(), Flat {
            override fun resolveEXP(): EXPGain = expGain
        }

        abstract class Params
    }

    open class Rules
}
