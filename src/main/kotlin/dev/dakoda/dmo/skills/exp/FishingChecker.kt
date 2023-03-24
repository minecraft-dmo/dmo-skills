package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.ModHelper.hasAnyEnchantment
import dev.dakoda.dmo.skills.Skill.Companion.FISHING
import net.minecraft.item.Items
import net.minecraft.registry.tag.ItemTags

object FishingChecker : AbstractFishingChecker() {
    init {
        registry[Items.LILY_PAD] = flat(4 to FISHING)
        registry[Items.BOWL] = flat(4 to FISHING)
        registry[Items.LEATHER] = flat(4 to FISHING)
        registry[Items.ROTTEN_FLESH] = flat(4 to FISHING)
        registry[Items.STICK] = flat(4 to FISHING)
        registry[Items.STRING] = flat(4 to FISHING)
        registry[Items.POTION] = flat(4 to FISHING)
        registry[Items.BONE] = flat(4 to FISHING)
        registry[Items.INK_SAC] = flat(4 to FISHING)
        registry[Items.TRIPWIRE_HOOK] = flat(4 to FISHING)
        registry[Items.BAMBOO] = flat(4 to FISHING)
        registry[Items.LEATHER_BOOTS] = flat(6 to FISHING)
        registry[ItemTags.FISHES] = callback { stack ->
            when (stack.item) {
                Items.COD, Items.SALMON -> 10 to FISHING
                Items.PUFFERFISH -> 15 to FISHING
                Items.TROPICAL_FISH -> 20 to FISHING
                else -> 10 to FISHING
            }
        }
        registry[Items.BOW] = callback { stack -> (if (stack.hasAnyEnchantment) 30 else 5) to FISHING }
        registry[Items.FISHING_ROD] = callback { stack -> (if (stack.hasAnyEnchantment) 30 else 5) to FISHING }
        registry[Items.ENCHANTED_BOOK] = flat(30 to FISHING)
        registry[Items.NAME_TAG] = flat(30 to FISHING)
        registry[Items.NAUTILUS_SHELL] = flat(30 to FISHING)
        registry[Items.SADDLE] = flat(30 to FISHING)
    }
}
