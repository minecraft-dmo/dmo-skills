package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.ModHelper.hasAnyEnchantment
import dev.dakoda.dmo.skills.Skill.Companion.FISHING
import dev.dakoda.dmo.skills.SubSkill
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
import net.minecraft.registry.tag.ItemTags.FISHES

object FishingEXPCheckerOld : EXPCheckerOld {

    fun determineEXPGain(itemStack: ItemStack): Pair<Int, SubSkill> {
        with(itemStack) {
            return if (eq(
                    LILY_PAD, BOWL, LEATHER, ROTTEN_FLESH,
                    STICK, STRING, POTION, BONE, INK_SAC,
                    TRIPWIRE_HOOK, BAMBOO
            )) 4 to FISHING
            else if (eq(LEATHER_BOOTS)) 6 to FISHING
            else if (of(FISHES)) {
                when (itemStack.item) {
                    COD, SALMON -> 10 to FISHING
                    PUFFERFISH -> 15 to FISHING
                    TROPICAL_FISH -> 20 to FISHING
                    else -> 10 to FISHING
                }
            } else if (eq(
                    BOW,
                    FISHING_ROD
                )) if (itemStack.hasAnyEnchantment) 30 to FISHING else 5 to FISHING
            else if (eq(ENCHANTED_BOOK, NAME_TAG, NAUTILUS_SHELL, SADDLE)) 30 to FISHING
            else 4 to FISHING
        }
    }
}