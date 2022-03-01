package dev.dakoda.dmo.skills.component

import dev.dakoda.dmo.skills.PlayerSkill
import dev.dakoda.dmo.skills.PlayerSkills
import dev.onyxstudios.cca.api.v3.component.Component

interface IPlayerSkillsComponent : Component {

    var skills: PlayerSkills

    fun increment(inc: Float, skill: PlayerSkill)
}
