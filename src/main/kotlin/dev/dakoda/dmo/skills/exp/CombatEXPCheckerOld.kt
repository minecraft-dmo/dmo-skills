package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.Skill.Companion.ARCHERY
import dev.dakoda.dmo.skills.Skill.Companion.HUNTER
import dev.dakoda.dmo.skills.Skill.Companion.MELEE
import dev.dakoda.dmo.skills.SubSkill
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags.AXES
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags.BOWS
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags.SWORDS
import net.minecraft.entity.Entity
import net.minecraft.entity.boss.WitherEntity
import net.minecraft.entity.boss.dragon.EnderDragonEntity
import net.minecraft.entity.mob.CreeperEntity
import net.minecraft.entity.mob.ElderGuardianEntity
import net.minecraft.entity.mob.EndermanEntity
import net.minecraft.entity.mob.EndermiteEntity
import net.minecraft.entity.mob.EvokerEntity
import net.minecraft.entity.mob.FlyingEntity
import net.minecraft.entity.mob.GuardianEntity
import net.minecraft.entity.mob.Hoglin
import net.minecraft.entity.mob.IllagerEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.mob.PhantomEntity
import net.minecraft.entity.mob.PiglinEntity
import net.minecraft.entity.mob.RavagerEntity
import net.minecraft.entity.mob.ShulkerEntity
import net.minecraft.entity.mob.SilverfishEntity
import net.minecraft.entity.mob.SkeletonEntity
import net.minecraft.entity.mob.SlimeEntity
import net.minecraft.entity.mob.SpiderEntity
import net.minecraft.entity.mob.VexEntity
import net.minecraft.entity.mob.WardenEntity
import net.minecraft.entity.mob.WitchEntity
import net.minecraft.entity.mob.WitherSkeletonEntity
import net.minecraft.entity.mob.ZombieEntity
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.item.ItemStack

object CombatEXPCheckerOld : EXPCheckerOld {

    private fun Int.determineEXPType(hand: ItemStack): Pair<Int, SubSkill>? {
        if (hand == ItemStack.EMPTY) return null
        with(hand) {
            return this@determineEXPType to (if (of(SWORDS) or of(AXES)) MELEE else if (of(BOWS)) ARCHERY else MELEE)
        }
    }

    object Attack {

        fun determineEXPGain(hand: ItemStack, entity: Entity): Pair<Int, SubSkill>? = when (entity) {
            is WitherEntity, is EnderDragonEntity -> 3
            is FlyingEntity -> 2
            else -> null
        }?.determineEXPType(hand)
    }

    object Kill {

        fun determineEXPGain(hand: ItemStack, entity: Entity): List<Pair<Int, SubSkill>?> = mutableListOf(
            when (entity) {
                is EndermiteEntity, is SilverfishEntity -> 1
                is PiglinEntity -> 4
                is WolfEntity -> 2
                is SlimeEntity -> when (entity.size) {
                    in (90..127) -> 4
                    in (60..89) -> 3
                    in (30..59) -> 2
                    else -> 1
                }
                is SpiderEntity -> 4
                is ZombieEntity, is CreeperEntity, is ShulkerEntity -> 5
                is SkeletonEntity, is WitchEntity, is Hoglin -> 7
                is ElderGuardianEntity -> 12
                is EndermanEntity, is GuardianEntity -> 8
                is IllagerEntity, is PhantomEntity, is VexEntity -> 6
                is RavagerEntity, is WitherSkeletonEntity -> 10
                is WardenEntity -> 45
                is WitherEntity -> 60
                is EnderDragonEntity -> 120
                is Monster -> 5
                else -> null
            }?.determineEXPType(hand)
        ).apply {
            when (entity) {
                is IllagerEntity -> {
                    if (entity.isPatrolLeader) add(15 to HUNTER)
                    if (entity is EvokerEntity) add(20 to HUNTER)
                }
                is WitchEntity -> add(7 to HUNTER)
                is EndermanEntity -> add(8 to HUNTER)
                is WitherSkeletonEntity -> (10 to HUNTER)
                is ElderGuardianEntity -> add(12 to HUNTER)
                is WardenEntity -> add(30 to HUNTER)
                is WitherEntity -> add(60 to HUNTER)
                is EnderDragonEntity -> add(120 to HUNTER)
            }
        }
    }
}