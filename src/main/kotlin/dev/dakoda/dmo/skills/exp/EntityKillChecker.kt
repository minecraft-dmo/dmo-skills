package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.Skill.Companion.VARIANT
import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.SlimeEntity
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.registry.tag.EntityTypeTags

object EntityKillChecker : AbstractEntityKillChecker(VARIANT) {
    init {
        registry[EntityType.ENDERMITE] = flat(1)
        registry[EntityType.SILVERFISH] = flat(1)
        registry[EntityType.PIGLIN] = flat(4)
        registry[EntityType.WOLF] = callback { _, player, killedEntity ->
            if ((killedEntity as WolfEntity).angryAt == player.uuid) {
                4
            } else if (killedEntity.angryAt != null) {
                3
            } else {
                2
            }
        }
        registry[EntityType.SLIME] = callback { _, _, killedEntity ->
            when ((killedEntity as SlimeEntity).size) {
                in (90..127) -> 4
                in (60..89) -> 3
                in (30..59) -> 2
                else -> 1
            }
        }
        registry[EntityType.SPIDER] = flat(4)
        registry[EntityType.ZOMBIE] = flat(5)
        registry[EntityType.CREEPER] = flat(5)
        registry[EntityType.SHULKER] = flat(5)
        registry[EntityType.SKELETON] = flat(7)
        registry[EntityType.WITCH] = flat(7)
        registry[EntityType.HOGLIN] = flat(7)
        registry[EntityType.ZOGLIN] = flat(7)
        registry[EntityType.ELDER_GUARDIAN] = flat(12)
        registry[EntityType.ENDERMAN] = flat(8)
        registry[EntityType.GUARDIAN] = flat(8)
        registry[EntityTypeTags.RAIDERS] = flat(6)
        registry[EntityType.PHANTOM] = flat(1)
        registry[EntityType.VEX] = flat(6)
        registry[EntityType.RAVAGER] = flat(10)
        registry[EntityType.WITHER_SKELETON] = flat(10)
        registry[EntityType.WARDEN] = flat(45)
        registry[EntityType.WITHER] = flat(60)
        registry[EntityType.ENDER_DRAGON] = flat(120)
    }
}
