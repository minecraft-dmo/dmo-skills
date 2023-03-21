package dev.dakoda.dmo.skills.component

import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.SubSkill
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import net.minecraft.nbt.NbtCompound

class SkillsTrackedComponent : ISkillsTrackedComponent, AutoSyncedComponent {

    override var trackedSkills: MutableMap<Skill, Int> = Skill.allSubSkills.associateWith { 0 }.toMutableMap()

    fun toggle(subSkill: SubSkill) {
        trackedSkills[subSkill] = if (trackedSkills[subSkill] == 1) 0 else 1
    }

    override fun readFromNbt(tag: NbtCompound) {
        val newMap = mutableMapOf<Skill, Boolean>()
        Skill.allSubSkills.forEach {
            newMap[it] = tag.getBoolean(it.name)
        }
    }

    override fun writeToNbt(tag: NbtCompound) {
        trackedSkills.forEach {
            tag.putInt(it.key.name, it.value)
        }
    }
}
