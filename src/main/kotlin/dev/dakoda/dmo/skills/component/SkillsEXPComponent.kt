package dev.dakoda.dmo.skills.component

import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.Skills
import dev.dakoda.dmo.skills.Skills.EXP
import dev.dakoda.dmo.skills.TrackableSkill
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import net.minecraft.nbt.NbtCompound

class SkillsEXPComponent : ISkillsEXPComponent, AutoSyncedComponent {

    override var skills: Skills = Skills.BLANK

    override fun readFromNbt(tag: NbtCompound) {
        val newMap = mutableMapOf<TrackableSkill, EXP>()
        Skill.all.forEach {
            newMap[it] = EXP(it).apply {
                with(tag.getSkill(it)) {
                    raw = this.getLong("exp")
                    level = this.getLong("level")
                }
            }
        }
        skills.values.clear()
        skills.values.putAll(newMap)
    }

    private fun NbtCompound.getSkill(skill: TrackableSkill) = this.getCompound(skill.name)

    override fun writeToNbt(tag: NbtCompound) {
        Skill.all.forEach {
            tag.put(
                it.name,
                NbtCompound().apply {
                    putLong("exp", skills.values[it]?.raw ?: 0)
                    putLong("level", skills.values[it]?.level ?: 1)
                }
            )
        }
    }
}
