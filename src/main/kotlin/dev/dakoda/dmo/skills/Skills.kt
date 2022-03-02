package dev.dakoda.dmo.skills

class Skills(
    val values: MutableMap<TrackableSkill, EXP> = mutableMapOf()
) {

    /**
     * These methods exist for auto-completion, because IntelliJ
     * can't suggest [TrackableSkill] enums.
     */
    fun increment(inc: Int, skill: Skill) = increment(inc, skill as TrackableSkill)
    fun increment(inc: Int, subSkill: Skill.Sub) = increment(inc, subSkill as TrackableSkill)

    private fun increment(inc: Int, trackableSkill: TrackableSkill) {
        if (trackableSkill is Skill) return
        if (trackableSkill in values.keys) {
            val before = values[trackableSkill]?.raw
            println("${trackableSkill.name} EXP was $before, increased to ")
            values[trackableSkill]?.raw = ((before ?: 0) + inc).also {
                println(it)
            }
        }
    }

    /**
     * These methods exist for auto-completion, because IntelliJ
     * can't suggest [TrackableSkill] enums.
     */
    fun levelOf(skill: Skill) = levelOf(skill as TrackableSkill)
    fun levelOf(subSkill: Skill.Sub) = levelOf(subSkill as TrackableSkill)

    private fun levelOf(trackableSkill: TrackableSkill): Int {
        if (trackableSkill is Skill) trackableSkill.updateRaw()
        return values[trackableSkill]?.level ?: -1
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
            const val expPerLevel = 200

            val NULL get() = EXP(Skill.NULL)
        }

        var raw: Int = 0

        val level: Int get() = (raw / expPerLevel)
    }

    companion object {
        val BLANK: Skills get() {
            val values: MutableMap<TrackableSkill, EXP> = mutableMapOf()
            Skill.all.forEach { trackableSkill: TrackableSkill ->
                values[trackableSkill] = EXP(trackableSkill)
            }
            return Skills(values)
        }
    }
}
