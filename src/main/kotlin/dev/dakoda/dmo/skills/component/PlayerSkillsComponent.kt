package dev.dakoda.dmo.skills.component

import dev.dakoda.dmo.skills.PlayerSkill
import dev.dakoda.dmo.skills.PlayerSkill.MINING
import dev.dakoda.dmo.skills.PlayerSkill.WOODCUTTING
import dev.dakoda.dmo.skills.PlayerSkills
import net.minecraft.nbt.NbtCompound

class PlayerSkillsComponent : IPlayerSkillsComponent {

    override var skills: PlayerSkills = PlayerSkills.blank()

    override fun increment(inc: Float, skill: PlayerSkill) = skills.increment(inc, skill)

    override fun readFromNbt(tag: NbtCompound) {
        skills = PlayerSkills.construct(
            woodcutting = tag.getSkill(WOODCUTTING),
            mining = tag.getSkill(MINING)
        )
    }

    fun NbtCompound.getSkill(skill: PlayerSkill) = this.getFloat(skill.name)

    override fun writeToNbt(tag: NbtCompound) = with(skills) {
        tag.putSkills()
    }
}
