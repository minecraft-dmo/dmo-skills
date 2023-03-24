package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.Skill.Companion.HUNTER
import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.EvokerEntity
import net.minecraft.entity.mob.IllagerEntity
import net.minecraft.registry.tag.EntityTypeTags

object EntityHuntChecker : AbstractEntityKillChecker(HUNTER) {
    init {
        registry[EntityTypeTags.RAIDERS] = callback { _, _, entity ->
            with(entity as IllagerEntity) {
                if (isPatrolLeader) {
                    15
                } else if (this is EvokerEntity) {
                    20
                } else {
                    null
                }
            }
        }
        registry[EntityType.WITCH] = flat(7)
        registry[EntityType.ENDERMAN] = flat(8)
        registry[EntityType.WITHER_SKELETON] = flat(10)
        registry[EntityType.ELDER_GUARDIAN] = flat(12)
        registry[EntityType.WARDEN] = flat(30)
        registry[EntityType.WITHER] = flat(60)
        registry[EntityType.ENDER_DRAGON] = flat(120)
    }
}
