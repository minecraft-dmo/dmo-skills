package dev.dakoda.dmo.skills.component

import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.Skills
import dev.dakoda.dmo.skills.Skills.EXP
import dev.dakoda.dmo.skills.TrackableSkill
import net.minecraft.nbt.NbtCompound

class PlayerSkillsComponent : IPlayerSkillsComponent {

    override var skills: Skills = Skills()

    override fun increment(inc: Int, skill: Skill) {
        skills.increment(inc, skill)
    }

    override fun readFromNbt(tag: NbtCompound) {
        val newMap = mutableMapOf<TrackableSkill, EXP>()
        Skill.all.forEach {
            newMap[it] = EXP(it).apply {
                raw = tag.getSkill(it)
            }
        }
        skills.values.clear()
        skills.values.putAll(newMap)
    }

    private fun NbtCompound.getSkill(skill: TrackableSkill) = this.getInt(skill.name)

    override fun writeToNbt(tag: NbtCompound) = with(skills) {
        Skill.all.forEach {
            tag.putInt(it.name, skills.values[it]?.raw ?: -1)
        }
    }
}
