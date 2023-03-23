package dev.dakoda.dmo.skills

import dev.dakoda.dmo.skills.exp.EXPGain

class Skills(

    val values: MutableMap<Skill, EXP> = mutableMapOf()
) {

    /**
     * These methods exist for auto-completion, because IntelliJ
     * can't suggest [TrackableSkill] enums.
     */
    fun increase(inc: Int, subSkill: SubSkill) = increase(inc, subSkill as Skill)

    fun increase(gain: EXPGain) = increase(gain.amount, gain.skill)
    fun increase(gain: Pair<Int, SubSkill>) = increase(gain.first, gain.second as Skill)

    private fun increase(inc: Int, skill: Skill) {
        if (skill is SkillCategory) return
        if (skill in values.keys) {
            val exp = values[skill] ?: return
            with(exp) {
                val i = (raw + inc)
                val l = this.level
                if (i >= EXP.perLevel) {
                    val levels = i / EXP.perLevel
                    val leftOver = i % EXP.perLevel
                    values[skill] = (
                        values[skill]?.apply {
                            raw = leftOver
                            level = l + levels
                        } ?: return
                        )
//                    println("${skill.name} level was $beforeLevel, increased to ${values[skill]?.level} ")
                } else {
                    values[skill] = (
                        values[skill]?.apply {
                            raw = i
                        } ?: return
                        )
                }
            }
//            println("${skill.name} EXP was $beforeEXP, increased to ${values[skill]?.raw}")
        }
    }

    fun levelOf(skill: Skill): Long {
        if (skill is SkillCategory) skill.updateRaw()
        return values[skill]?.level ?: 0L
    }

    private fun SkillCategory.updateRaw() {
        values[this]?.raw = this.subSkills.sumOf {
            values[it]?.raw ?: 0
        }
    }

    operator fun get(skill: Skill) = progressOf(skill)

    fun progressOf(skill: Skill): EXP {
        if (skill is SkillCategory) skill.updateRaw()
        return values[skill] ?: EXP.NULL
    }

    fun categories(): List<EXP> = Skill.allCategories.mapNotNull {
        it.updateRaw()
        values[it]
    }

    fun subSkills(): List<EXP> = Skill.allSubSkills.mapNotNull {
        values[it]
    }

    fun subSkills(category: SkillCategory): List<EXP> = category.subSkills.mapNotNull {
        values[it]
    }

    fun all(): List<EXP> = categories() + subSkills()

    class EXP(val skill: Skill) {

        companion object {
            const val perLevel = 100

            val NULL get() = EXP(Skill.NULL)
        }

        var raw: Long = 0
        var level: Long = 1
    }

    companion object {
        val BLANK: Skills
            get() {
                val values: MutableMap<Skill, EXP> = mutableMapOf()
                Skill.all.forEach { skill: Skill ->
                    values[skill] = EXP(skill)
                }
                return Skills(values)
            }
    }
}
