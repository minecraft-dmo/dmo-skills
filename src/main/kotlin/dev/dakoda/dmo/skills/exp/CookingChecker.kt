package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.exp.CookingChecker.flat
import net.minecraft.item.Items

object CookingChecker : AbstractCookingChecker({
    registry[Items.BREAD] = flat(2 to Skill.COOKING)
    registry[Items.BAKED_POTATO] = flat(5 to Skill.COOKING)
    registry[Items.CAKE] = flat(20 to Skill.COOKING)
    registry[Items.COOKED_BEEF] = flat(5 to Skill.COOKING)
    registry[Items.COOKED_CHICKEN] = flat(5 to Skill.COOKING)
    registry[Items.COOKED_COD] = flat(5 to Skill.COOKING)
    registry[Items.COOKED_MUTTON] = flat(5 to Skill.COOKING)
    registry[Items.COOKED_PORKCHOP] = flat(5 to Skill.COOKING)
    registry[Items.COOKED_RABBIT] = flat(5 to Skill.COOKING)
    registry[Items.COOKED_SALMON] = flat(5 to Skill.COOKING)
})
