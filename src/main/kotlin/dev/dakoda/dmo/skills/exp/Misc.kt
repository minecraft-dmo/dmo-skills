package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.SubSkill

val Pair<Int, SubSkill>.expGain get() = EXPGain(this)