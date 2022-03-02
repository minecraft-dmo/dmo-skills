package dev.dakoda.dmo.skills.component

import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.Skills
import dev.onyxstudios.cca.api.v3.component.Component

interface IPlayerSkillsComponent : Component {

    var skills: Skills

    fun increment(inc: Int, skill: Skill)
}
