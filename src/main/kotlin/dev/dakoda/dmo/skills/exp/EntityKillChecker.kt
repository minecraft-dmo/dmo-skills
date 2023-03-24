package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.DMOSkills.CONFIG
import dev.dakoda.dmo.skills.Skill.Companion.VARIANT
import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.SlimeEntity
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.registry.tag.EntityTypeTags

object EntityKillChecker : AbstractEntityKillChecker(VARIANT, useBackupEXP = true) {
    init {
        val combat = CONFIG.exp.combat.sources.killing
        registry[EntityType.ENDERMITE] = flat(combat.endermite)
        registry[EntityType.SILVERFISH] = flat(combat.silverfish)
        registry[EntityType.PIGLIN] = flat(combat.piglin)
        registry[EntityType.WOLF] = callback { _, player, killedEntity ->
            if ((killedEntity as WolfEntity).angryAt == player.uuid) {
                combat.wolfAngryAtYou
            } else if (killedEntity.angryAt != null) {
                combat.wolfAngry
            } else {
                combat.wolf
            }
        }
        registry[EntityType.SLIME] = callback { _, _, killedEntity ->
            when ((killedEntity as SlimeEntity).size) {
                in (90..127) -> combat.slimeBig
                in (60..89) -> combat.slimeMedium
                in (30..59) -> combat.slimeSmall
                else -> combat.slimeTiny
            }
        }
        registry[EntityType.SPIDER] = flat(combat.spider)
        registry[EntityType.CAVE_SPIDER] = flat(combat.caveSpider)
        registry[EntityType.ZOMBIE] = flat(combat.zombie)
        registry[EntityType.CREEPER] = flat(combat.creeper)
        registry[EntityType.SHULKER] = flat(combat.shulker)
        registry[EntityType.SKELETON] = flat(combat.skeleton)
        registry[EntityType.WITCH] = flat(combat.witch)
        registry[EntityType.HOGLIN] = flat(combat.hoglin)
        registry[EntityType.ZOGLIN] = flat(combat.zoglin)
        registry[EntityType.ELDER_GUARDIAN] = flat(combat.elderGuardian)
        registry[EntityType.ENDERMAN] = flat(combat.enderman)
        registry[EntityType.GUARDIAN] = flat(combat.guardian)
        registry[EntityTypeTags.RAIDERS] = flat(combat.pillagers)
        registry[EntityType.PHANTOM] = flat(combat.phantom)
        registry[EntityType.VEX] = flat(combat.vex)
        registry[EntityType.RAVAGER] = flat(combat.ravager)
        registry[EntityType.WITHER_SKELETON] = flat(combat.witherSkeleton)
        registry[EntityType.WARDEN] = flat(combat.warden)
        registry[EntityType.WITHER] = flat(combat.wither)
        registry[EntityType.ENDER_DRAGON] = flat(combat.enderDragon)
    }
}
