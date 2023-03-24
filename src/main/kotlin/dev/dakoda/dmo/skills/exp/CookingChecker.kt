package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.DMOSkills.CONFIG
import dev.dakoda.dmo.skills.Skill.Companion.COOKING
import net.minecraft.item.Items

object CookingChecker : AbstractCookingChecker() {
    init {
        val cooking = CONFIG.exp.cooking.sources.crafting
        registry[Items.BREAD] = flat(cooking.bread to COOKING)
        registry[Items.BAKED_POTATO] = flat(cooking.bakedPotato to COOKING)
        registry[Items.CAKE] = flat(cooking.cake to COOKING)
        registry[Items.COOKED_BEEF] = flat(cooking.cookedBeef to COOKING)
        registry[Items.COOKED_CHICKEN] = flat(cooking.cookedChicken to COOKING)
        registry[Items.COOKED_COD] = flat(cooking.cookedCod to COOKING)
        registry[Items.COOKED_MUTTON] = flat(cooking.cookedMutton to COOKING)
        registry[Items.COOKED_PORKCHOP] = flat(cooking.cookedPorkchop to COOKING)
        registry[Items.COOKED_RABBIT] = flat(cooking.cookedRabbit to COOKING)
        registry[Items.COOKED_SALMON] = flat(cooking.cookedSalmon to COOKING)
    }
}
