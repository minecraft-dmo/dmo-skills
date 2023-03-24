package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.DMOSkills.CONFIG
import dev.dakoda.dmo.skills.DMOSkills.hasAnyEnchantment
import dev.dakoda.dmo.skills.Skill.Companion.FISHING
import net.minecraft.item.Items
import net.minecraft.registry.tag.ItemTags

object FishingChecker : AbstractFishingChecker() {
    init {
        val fishing = CONFIG.exp.fishing.sources.catching
        registry[Items.LILY_PAD] = flat(fishing.lilyPad to FISHING)
        registry[Items.BOWL] = flat(fishing.bowl to FISHING)
        registry[Items.LEATHER] = flat(fishing.leather to FISHING)
        registry[Items.ROTTEN_FLESH] = flat(fishing.rottenFlesh to FISHING)
        registry[Items.STICK] = flat(fishing.stick to FISHING)
        registry[Items.STRING] = flat(fishing.string to FISHING)
        registry[Items.POTION] = flat(fishing.potion to FISHING)
        registry[Items.BONE] = flat(fishing.bone to FISHING)
        registry[Items.INK_SAC] = flat(fishing.inkSac to FISHING)
        registry[Items.TRIPWIRE_HOOK] = flat(fishing.tripwireHook to FISHING)
        registry[Items.BAMBOO] = flat(fishing.bamboo to FISHING)
        registry[Items.LEATHER_BOOTS] = flat(fishing.leatherBoots to FISHING)
        registry[ItemTags.FISHES] = callback { stack ->
            when (stack.item) {
                Items.COD, Items.SALMON -> fishing.codSalmon to FISHING
                Items.PUFFERFISH -> fishing.pufferFish to FISHING
                Items.TROPICAL_FISH -> fishing.tropicalFish to FISHING
                else -> fishing.fishMisc to FISHING
            }
        }
        registry[Items.BOW] = callback { stack -> (if (stack.hasAnyEnchantment) fishing.bow * fishing.enchantedMultiplier else fishing.bow) to FISHING }
        registry[Items.FISHING_ROD] = callback { stack -> (if (stack.hasAnyEnchantment) fishing.fishingRod * fishing.enchantedMultiplier else fishing.fishingRod) to FISHING }
        registry[Items.ENCHANTED_BOOK] = flat(fishing.enchantedBook to FISHING)
        registry[Items.NAME_TAG] = flat(fishing.nameTag to FISHING)
        registry[Items.NAUTILUS_SHELL] = flat(fishing.nautilusShell to FISHING)
        registry[Items.SADDLE] = flat(fishing.saddle to FISHING)
    }
}
