package dev.dakoda.dmo.skills.component

import dev.dakoda.dmo.skills.Skill
import dev.onyxstudios.cca.api.v3.component.Component

interface ISkillsTrackedComponent : Component {

    var trackedSkills: MutableMap<Skill, Int>
}
