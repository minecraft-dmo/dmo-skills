package dev.dakoda.dmo.skills

import dev.dakoda.dmo.skills.ModHelper.hasAnyEnchantment
import dev.dakoda.dmo.skills.ModHelper.horizontals
import dev.dakoda.dmo.skills.Skill.Companion.ARCHERY
import dev.dakoda.dmo.skills.Skill.Companion.CULTIVATION
import dev.dakoda.dmo.skills.Skill.Companion.FISHING
import dev.dakoda.dmo.skills.Skill.Companion.HUNTER
import dev.dakoda.dmo.skills.Skill.Companion.LUMBERING
import dev.dakoda.dmo.skills.Skill.Companion.MELEE
import dev.dakoda.dmo.skills.Skill.Companion.MINING
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags.AXES
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags.SWORDS
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks.ANCIENT_DEBRIS
import net.minecraft.block.Blocks.MELON
import net.minecraft.block.Blocks.NETHER_GOLD_ORE
import net.minecraft.block.Blocks.NETHER_QUARTZ_ORE
import net.minecraft.block.Blocks.PUMPKIN
import net.minecraft.block.CropBlock
import net.minecraft.block.StemBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.boss.WitherEntity
import net.minecraft.entity.boss.dragon.EnderDragonEntity
import net.minecraft.entity.mob.CreeperEntity
import net.minecraft.entity.mob.ElderGuardianEntity
import net.minecraft.entity.mob.EndermanEntity
import net.minecraft.entity.mob.EndermiteEntity
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
import net.minecraft.entity.mob.WitchEntity
import net.minecraft.entity.mob.WitherSkeletonEntity
import net.minecraft.entity.mob.ZombieEntity
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items.BAMBOO
import net.minecraft.item.Items.BONE
import net.minecraft.item.Items.BOW
import net.minecraft.item.Items.BOWL
import net.minecraft.item.Items.COD
import net.minecraft.item.Items.ENCHANTED_BOOK
import net.minecraft.item.Items.FISHING_ROD
import net.minecraft.item.Items.INK_SAC
import net.minecraft.item.Items.LEATHER
import net.minecraft.item.Items.LEATHER_BOOTS
import net.minecraft.item.Items.LILY_PAD
import net.minecraft.item.Items.NAME_TAG
import net.minecraft.item.Items.NAUTILUS_SHELL
import net.minecraft.item.Items.POTION
import net.minecraft.item.Items.PUFFERFISH
import net.minecraft.item.Items.ROTTEN_FLESH
import net.minecraft.item.Items.SADDLE
import net.minecraft.item.Items.SALMON
import net.minecraft.item.Items.STICK
import net.minecraft.item.Items.STRING
import net.minecraft.item.Items.TRIPWIRE_HOOK
import net.minecraft.item.Items.TROPICAL_FISH
import net.minecraft.tag.BlockTags.COAL_ORES
import net.minecraft.tag.BlockTags.COPPER_ORES
import net.minecraft.tag.BlockTags.CROPS
import net.minecraft.tag.BlockTags.DIAMOND_ORES
import net.minecraft.tag.BlockTags.EMERALD_ORES
import net.minecraft.tag.BlockTags.GOLD_ORES
import net.minecraft.tag.BlockTags.IRON_ORES
import net.minecraft.tag.BlockTags.LAPIS_ORES
import net.minecraft.tag.BlockTags.LOGS
import net.minecraft.tag.BlockTags.PICKAXE_MINEABLE
import net.minecraft.tag.BlockTags.REDSTONE_ORES
import net.minecraft.tag.ItemTags.FISHES
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object EXPChecker {

    object BlockBreak {

        fun determineEXPGain(
            world: World,
            blockPos: BlockPos,
            blockState: BlockState,
            usedSilkTouch: Boolean
        ): Pair<Int, SubSkill>? {
            fun Pair<Int, SubSkill>?.noSilk() = this.takeIf { !usedSilkTouch }

            return when (blockState.block) {
                // MINING
                in PICKAXE_MINEABLE -> when (blockState.block) {
                    in COAL_ORES -> (1 to MINING).noSilk()
                    in COPPER_ORES, NETHER_QUARTZ_ORE -> (2 to MINING).noSilk()
                    in IRON_ORES -> (3 to MINING).noSilk()
                    in LAPIS_ORES, in REDSTONE_ORES -> (5 to MINING).noSilk()
                    in GOLD_ORES -> (if (blockState.block == NETHER_GOLD_ORE) 1 to MINING else 5 to MINING).noSilk()
                    in DIAMOND_ORES -> (8 to MINING).noSilk()
                    in EMERALD_ORES -> (12 to MINING).noSilk()
                    ANCIENT_DEBRIS -> 10 to MINING
                    else -> null
                }
                // LUMBERING
                in LOGS -> 1 to LUMBERING
                // CULTIVATION
                in CROPS -> if (blockState.block is CropBlock) {
                    if ((blockState.block as CropBlock).isMature(blockState)) {
                        6 to CULTIVATION
                    } else null
                } else if (blockState.block !is StemBlock) 1 to CULTIVATION else null
                PUMPKIN -> if (blockPos.horizontals(world).any { it.block is StemBlock }) {
                    6 to CULTIVATION
                } else null
                MELON -> (6 to CULTIVATION).noSilk()
                else -> null
            }
        }
    }

    object Fishing {

        fun determineEXPGain(itemStack: ItemStack): Pair<Int, SubSkill> = when (itemStack.item) {
            LILY_PAD, BOWL, LEATHER, ROTTEN_FLESH, STICK, STRING,
            POTION, BONE, INK_SAC, TRIPWIRE_HOOK, BAMBOO -> 4 to FISHING
            LEATHER_BOOTS -> 6 to FISHING
            in FISHES -> when (itemStack.item) {
                COD, SALMON -> 10 to FISHING
                PUFFERFISH -> 15 to FISHING
                TROPICAL_FISH -> 20 to FISHING
                else -> 10 to FISHING
            }
            BOW, FISHING_ROD -> if (itemStack.hasAnyEnchantment) 30 to FISHING else 5 to FISHING
            ENCHANTED_BOOK, NAME_TAG, NAUTILUS_SHELL, SADDLE -> 30 to FISHING
            else -> 4 to FISHING
        }
    }

    object Combat {

        private fun Int.determineEXPType(hand: ItemStack) = when (hand.item) {
            in SWORDS.values(), in AXES.values() -> MELEE
            BOW -> ARCHERY
            else -> MELEE
        }.let { this to it }

        object Attack {

            fun determineEXPGain(hand: ItemStack, entity: Entity): Pair<Int, SubSkill> = when (entity) {
                is WitherEntity, is EnderDragonEntity -> 3
                is FlyingEntity -> 2
                else -> 1
            }.determineEXPType(hand)
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
                    is WitherEntity -> 60
                    is EnderDragonEntity -> 120
                    is Monster -> 5
                    else -> null
                }?.determineEXPType(hand)
            ).apply {
                when (entity) {
                    is WitherEntity -> add(60 to HUNTER)
                    is EnderDragonEntity -> add(120 to HUNTER)
                }
            }
        }
    }
}
