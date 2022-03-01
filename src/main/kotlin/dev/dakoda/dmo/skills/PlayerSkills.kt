package dev.dakoda.dmo.skills

import dev.dakoda.dmo.skills.PlayerSkill.MINING
import dev.dakoda.dmo.skills.PlayerSkill.WOODCUTTING
import dev.onyxstudios.cca.api.v3.component.Component
import net.minecraft.nbt.NbtCompound

data class PlayerSkills(
    val values: MutableMap<PlayerSkill, Float> = mutableMapOf(),
) {
    operator fun get(skill: PlayerSkill) = values[skill]

    fun increment(inc: Float, skill: PlayerSkill) {
        val f = values[skill] ?: return
        values[skill] = f + inc
    }

    fun NbtCompound.putSkills() {
        PlayerSkill.values().forEach {
            this.putSkill(it)
        }
    }

    fun NbtCompound.putSkill(skill: PlayerSkill) {
        val f = values[skill] ?: 0f
        this.putFloat(skill.name, f)
    }

    companion object {
        fun blank() = construct(0f, 0f)

        fun construct(
            woodcutting: Float,
            mining: Float,
        ) = PlayerSkills(
            mutableMapOf(
                WOODCUTTING to woodcutting,
                MINING to mining,
            )
        )
    }
}
