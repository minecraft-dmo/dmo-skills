package dev.dakoda.dmo.skills

import dev.dakoda.dmo.skills.Skill.Sub.ALCHEMY
import dev.dakoda.dmo.skills.Skill.Sub.ANIMAL_CARE
import dev.dakoda.dmo.skills.Skill.Sub.ARCHERY
import dev.dakoda.dmo.skills.Skill.Sub.ARCHITECTURE
import dev.dakoda.dmo.skills.Skill.Sub.CARTOGRAPHY
import dev.dakoda.dmo.skills.Skill.Sub.COOKING
import dev.dakoda.dmo.skills.Skill.Sub.CULTIVATION
import dev.dakoda.dmo.skills.Skill.Sub.ENCHANTING
import dev.dakoda.dmo.skills.Skill.Sub.FORAGING
import dev.dakoda.dmo.skills.Skill.Sub.LUMBERING
import dev.dakoda.dmo.skills.Skill.Sub.MELEE
import dev.dakoda.dmo.skills.Skill.Sub.METALWORK
import dev.dakoda.dmo.skills.Skill.Sub.MINING
import dev.dakoda.dmo.skills.Skill.Sub.TRADING
import net.minecraft.text.TranslatableText

enum class Skill(vararg val subSkills: Sub) : TrackableSkill {

    NULL,
    GATHERING(LUMBERING, MINING, FORAGING),
    FARMING(CULTIVATION, ANIMAL_CARE),
    MERCHANT(TRADING),
    EXPLORER(CARTOGRAPHY, ARCHITECTURE),
    ADVENTURE(MELEE, ARCHERY),
    CRAFTING(ALCHEMY, ENCHANTING, METALWORK, COOKING);

    init {
        this.subSkills.forEach {
            it.parent = this
        }
    }

    override val translatableText get() = TranslatableText("dmo.skills.${name.lowercase()}")

    companion object {
        val all: List<TrackableSkill> get() = (values().toList() + Sub.values())
    }

    enum class Sub : TrackableSkill {

        LUMBERING, MINING, FORAGING,
        CULTIVATION, ANIMAL_CARE,
        TRADING,
        CARTOGRAPHY, ARCHITECTURE,
        MELEE, ARCHERY,
        ALCHEMY, ENCHANTING, METALWORK, COOKING;

        lateinit var parent: Skill

        override val translatableText get() = TranslatableText("dmo.skills.${parent.name.lowercase()}.${name.lowercase()}")
    }
}
