package dev.dakoda.dmo.skills.component

import dev.dakoda.dmo.skills.TrackableSkill
import dev.onyxstudios.cca.api.v3.component.Component

interface ISkillsTrackedComponent : Component {

    var trackedSkills: MutableMap<TrackableSkill, Int>
}
