package dev.dakoda.dmo.skills.component

import dev.dakoda.dmo.skills.Skill
import dev.onyxstudios.cca.api.v3.component.Component

interface ISkillsDiscoveredComponent : Component {

    var skillsDiscovered: MutableMap<Skill, Boolean>
}
