package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.DMOSkills.CONFIG
import dev.dakoda.dmo.skills.Skill.Companion.ALCHEMY
import net.minecraft.potion.Potions

object PotionBrewingChecker : AbstractPotionBrewingChecker() {
    init {
        // -- Base potions --
        registry[Potions.AWKWARD] = flat(CONFIG.exp.alchemy.sources.brewing.awkward to ALCHEMY)
        registry[Potions.MUNDANE] = flat(CONFIG.exp.alchemy.sources.brewing.mundane to ALCHEMY) // Not used for anything
        registry[Potions.THICK] = flat(CONFIG.exp.alchemy.sources.brewing.thick to ALCHEMY) // Not used for anything

        // -- Base effects --
        val baseEffectEXP = CONFIG.exp.alchemy.sources.brewing.standardEffects
        registry[Potions.SWIFTNESS] = flat(baseEffectEXP to ALCHEMY)
        registry[Potions.SLOWNESS] = flat(baseEffectEXP to ALCHEMY)
        registry[Potions.LEAPING] = flat(baseEffectEXP to ALCHEMY)
        registry[Potions.STRENGTH] = flat(baseEffectEXP to ALCHEMY)
        registry[Potions.HEALING] = flat(baseEffectEXP to ALCHEMY)
        registry[Potions.HARMING] = flat(baseEffectEXP to ALCHEMY)
        registry[Potions.POISON] = flat(baseEffectEXP to ALCHEMY)
        registry[Potions.REGENERATION] = flat(baseEffectEXP to ALCHEMY)
        registry[Potions.FIRE_RESISTANCE] = flat(baseEffectEXP to ALCHEMY)
        registry[Potions.WATER_BREATHING] = flat(baseEffectEXP to ALCHEMY)
        registry[Potions.NIGHT_VISION] = flat(baseEffectEXP to ALCHEMY)
        registry[Potions.INVISIBILITY] = flat(baseEffectEXP to ALCHEMY)
        registry[Potions.TURTLE_MASTER] = flat(baseEffectEXP to ALCHEMY)
        registry[Potions.SLOW_FALLING] = flat(baseEffectEXP to ALCHEMY)
        registry[Potions.WEAKNESS] = flat(baseEffectEXP to ALCHEMY)

        // -- Extended duration --
        val extendedDurationEXP = CONFIG.exp.alchemy.sources.brewing.extendedDuration
        registry[Potions.LONG_SWIFTNESS] = flat(extendedDurationEXP to ALCHEMY)
        registry[Potions.LONG_SLOWNESS] = flat(extendedDurationEXP to ALCHEMY)
        registry[Potions.LONG_LEAPING] = flat(extendedDurationEXP to ALCHEMY)
        registry[Potions.LONG_STRENGTH] = flat(extendedDurationEXP to ALCHEMY)
        registry[Potions.LONG_POISON] = flat(extendedDurationEXP to ALCHEMY)
        registry[Potions.LONG_REGENERATION] = flat(extendedDurationEXP to ALCHEMY)
        registry[Potions.LONG_FIRE_RESISTANCE] = flat(extendedDurationEXP to ALCHEMY)
        registry[Potions.LONG_WATER_BREATHING] = flat(extendedDurationEXP to ALCHEMY)
        registry[Potions.LONG_NIGHT_VISION] = flat(extendedDurationEXP to ALCHEMY)
        registry[Potions.LONG_INVISIBILITY] = flat(extendedDurationEXP to ALCHEMY)
        registry[Potions.LONG_TURTLE_MASTER] = flat(extendedDurationEXP to ALCHEMY)
        registry[Potions.LONG_SLOW_FALLING] = flat(extendedDurationEXP to ALCHEMY)
        registry[Potions.LONG_WEAKNESS] = flat(extendedDurationEXP to ALCHEMY)

        // -- Increased power --
        val increasedPowerEXP = CONFIG.exp.alchemy.sources.brewing.increasedPower
        registry[Potions.STRONG_SWIFTNESS] = flat(increasedPowerEXP to ALCHEMY)
        registry[Potions.STRONG_SLOWNESS] = flat(increasedPowerEXP to ALCHEMY)
        registry[Potions.STRONG_LEAPING] = flat(increasedPowerEXP to ALCHEMY)
        registry[Potions.STRONG_STRENGTH] = flat(increasedPowerEXP to ALCHEMY)
        registry[Potions.STRONG_HEALING] = flat(increasedPowerEXP to ALCHEMY)
        registry[Potions.STRONG_HARMING] = flat(increasedPowerEXP to ALCHEMY)
        registry[Potions.STRONG_POISON] = flat(increasedPowerEXP to ALCHEMY)
        registry[Potions.STRONG_REGENERATION] = flat(increasedPowerEXP to ALCHEMY)
        registry[Potions.STRONG_TURTLE_MASTER] = flat(increasedPowerEXP to ALCHEMY)
    }
}
