package dev.dakoda.dmo.skills

class Skills(

    val values: MutableMap<TrackableSkill, EXP> = mutableMapOf()
) {

    /**
     * These methods exist for auto-completion, because IntelliJ
     * can't suggest [TrackableSkill] enums.
     */
    fun increase(inc: Int, subSkill: Skill.Sub) = increase(inc, subSkill as TrackableSkill)

    private fun increase(inc: Int, trackableSkill: TrackableSkill) {
        if (trackableSkill is Skill) return
        if (trackableSkill in values.keys) {
            val beforeLevel = values[trackableSkill]?.level
            val beforeEXP = values[trackableSkill]?.raw

            val exp = values[trackableSkill] ?: return
            with(exp) {
                val i = (raw + inc)
                val l = this.level
                if (i >= EXP.perLevel) {
                    val levels = i / EXP.perLevel
                    val leftOver = i % EXP.perLevel
                    values[trackableSkill] = (
                        values[trackableSkill]?.apply {
                            raw = leftOver
                            level = l + levels
                        } ?: return
                        )
                    println("${trackableSkill.name} level was $beforeLevel, increased to ${values[trackableSkill]?.level} ")
                } else {
                    values[trackableSkill] = (
                        values[trackableSkill]?.apply {
                            raw = i
                        } ?: return
                        )
                }
            }
            println("${trackableSkill.name} EXP was $beforeEXP, increased to ${values[trackableSkill]?.raw}")
        }
    }

    /**
     * These methods exist for auto-completion, because IntelliJ
     * can't suggest [TrackableSkill] enums.
     */
    fun levelOf(skill: Skill) = levelOf(skill as TrackableSkill)
    fun levelOf(subSkill: Skill.Sub) = levelOf(subSkill as TrackableSkill)

    private fun levelOf(trackableSkill: TrackableSkill): Long {
        if (trackableSkill is Skill) trackableSkill.updateRaw()
        return values[trackableSkill]?.level ?: 0L
    }

    private fun Skill.updateRaw() {
        values[this]?.raw = this.subSkills.sumOf {
            values[it]?.raw ?: 0
        }
    }

    operator fun get(skill: Skill) = progressOf(skill)
    operator fun get(subSkill: Skill.Sub) = progressOf(subSkill)
    operator fun get(trackableSkill: TrackableSkill) = progressOf(trackableSkill)

    fun progressOf(trackableSkill: TrackableSkill): EXP {
        if (trackableSkill is Skill) trackableSkill.updateRaw()
        return values[trackableSkill] ?: EXP.NULL
    }

    fun categories(): List<EXP> = Skill.values().mapNotNull {
        it.updateRaw()
        values[it]
    }

    fun subSkills(): List<EXP> = Skill.Sub.values().mapNotNull {
        values[it]
    }

    fun subSkills(category: Skill): List<EXP> = category.subSkills.mapNotNull {
        values[it]
    }

    fun all(): List<EXP> = categories() + subSkills()

    class EXP(val skill: TrackableSkill) {

        /**
         * These constructors exist for auto-completion, because IntelliJ
         * can't suggest [TrackableSkill] enums.
         */
        constructor(skill: Skill) : this(skill as TrackableSkill)
        constructor(subSkill: Skill.Sub) : this(subSkill as TrackableSkill)

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
                val values: MutableMap<TrackableSkill, EXP> = mutableMapOf()
                Skill.all.forEach { trackableSkill: TrackableSkill ->
                    values[trackableSkill] = EXP(trackableSkill)
                }
                return Skills(values)
            }
    }
}
