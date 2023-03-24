package dev.dakoda.dmo.skills.config

import com.google.gson.annotations.SerializedName
import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.Skill.Companion.ALCHEMY
import dev.dakoda.dmo.skills.Skill.Companion.ANIMAL_CARE
import dev.dakoda.dmo.skills.Skill.Companion.CARTOGRAPHY
import dev.dakoda.dmo.skills.Skill.Companion.COOKING
import dev.dakoda.dmo.skills.Skill.Companion.CULTIVATION
import dev.dakoda.dmo.skills.Skill.Companion.DUNGEONEER
import dev.dakoda.dmo.skills.Skill.Companion.ENCHANTING
import dev.dakoda.dmo.skills.Skill.Companion.FISHING
import dev.dakoda.dmo.skills.Skill.Companion.FORAGING
import dev.dakoda.dmo.skills.Skill.Companion.HUNTER
import dev.dakoda.dmo.skills.Skill.Companion.LUMBERING
import dev.dakoda.dmo.skills.Skill.Companion.MELEE
import dev.dakoda.dmo.skills.Skill.Companion.METALWORK
import dev.dakoda.dmo.skills.Skill.Companion.MINING
import dev.dakoda.dmo.skills.Skill.Companion.RANGER
import dev.dakoda.dmo.skills.Skill.Companion.TRADING
import dev.dakoda.dmo.skills.config.dungeoneer.DMOConfigDungeoneer
import dev.dakoda.dmo.skills.config.dungeoneer.DMOConfigGeneric

class DMOSkillsConfig {

    companion object {
        val default: DMOSkillsConfig get() = DMOSkillsConfig()
    }

    @SerializedName("discovery")
    var discovery = Discovery()

    @SerializedName("experience")
    var exp = Experience()

    class Experience {
        @SerializedName("lumbering")
        var lumbering = DMOConfigGeneric()

        @SerializedName("mining")
        var mining = DMOConfigGeneric()

        @SerializedName("foraging")
        var foraging = DMOConfigGeneric()

        @SerializedName("fishing")
        var fishing = DMOConfigGeneric()

        @SerializedName("cultivation")
        var cultivation = DMOConfigGeneric()

        @SerializedName("animal_care")
        var animalCare = DMOConfigGeneric()

        @SerializedName("trading")
        var trading = DMOConfigGeneric()

        @SerializedName("cartography")
        var cartography = DMOConfigGeneric()

        @SerializedName("dungeoneer")
        var dungeoneer = DMOConfigDungeoneer()

        @SerializedName("melee")
        var melee = DMOConfigGeneric()

        @SerializedName("archery")
        var archery = DMOConfigGeneric()

        @SerializedName("hunter")
        var hunter = DMOConfigGeneric()

        @SerializedName("alchemy")
        var alchemy = DMOConfigGeneric()

        @SerializedName("enchanting")
        var enchanting = DMOConfigGeneric()

        @SerializedName("metalwork")
        var metalwork = DMOConfigGeneric()

        @SerializedName("cooking")
        var cooking = DMOConfigGeneric()
    }

    class Discovery {
        @SerializedName("toasts_enabled")
        var toastsEnabled: Boolean = false

        @SerializedName("toast_duration_seconds")
        var toastDurationSeconds: Int = 5
            get() = if (field <= 0) 5 else field
    }

    fun isDiscoveredByDefault(skill: Skill) = !when (skill) {
        LUMBERING -> exp.lumbering.defaultHidden
        MINING -> exp.mining.defaultHidden
        FORAGING -> exp.foraging.defaultHidden
        FISHING -> exp.fishing.defaultHidden
        CULTIVATION -> exp.cultivation.defaultHidden
        ANIMAL_CARE -> exp.animalCare.defaultHidden
        TRADING -> exp.trading.defaultHidden
        CARTOGRAPHY -> exp.cartography.defaultHidden
        DUNGEONEER -> exp.dungeoneer.defaultHidden
        MELEE -> exp.melee.defaultHidden
        RANGER -> exp.archery.defaultHidden
        HUNTER -> exp.hunter.defaultHidden
        ALCHEMY -> exp.alchemy.defaultHidden
        ENCHANTING -> exp.enchanting.defaultHidden
        METALWORK -> exp.metalwork.defaultHidden
        COOKING -> exp.cooking.defaultHidden
        else -> false
    }
}
