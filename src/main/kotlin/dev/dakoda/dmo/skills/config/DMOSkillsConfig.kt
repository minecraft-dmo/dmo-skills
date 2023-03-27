package dev.dakoda.dmo.skills.config

import com.google.gson.annotations.SerializedName
import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.Skill.Companion.ALCHEMY
import dev.dakoda.dmo.skills.Skill.Companion.ANIMAL_CARE
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

class DMOSkillsConfig {

    companion object {
        val default: DMOSkillsConfig get() = DMOSkillsConfig()
    }

    @SerializedName("discovery")
    var discovery = Discovery()

    @SerializedName("experience")
    var exp = Experience()

    class Discovery {
        @SerializedName("toasts_enabled")
        var toastsEnabled: Boolean = false

        @SerializedName("toast_duration_seconds")
        var toastDurationSeconds: Int = 5
            get() = if (field <= 0) 5 else field
    }

    class Experience {
        @SerializedName("lumbering")
        var lumbering = DMOConfigLumbering()

        @SerializedName("mining")
        var mining = DMOConfigMining()

        @SerializedName("foraging")
        var foraging = DMOConfigForaging()

        @SerializedName("fishing")
        var fishing = DMOConfigFishing()

        @SerializedName("cultivation")
        var cultivation = DMOConfigCultivation()

        @SerializedName("animal_care")
        var animalCare = DMOConfigAnimalCare()

        @SerializedName("trading")
        var trading = DMOConfigTrading()

        @SerializedName("dungeoneer")
        var dungeoneer = DMOConfigDungeoneer()

        @SerializedName("combat")
        var combat = DMOConfigCombat()

        @SerializedName("hunter")
        var hunter = DMOConfigHunter()

        @SerializedName("alchemy")
        var alchemy = DMOConfigAlchemy()

        @SerializedName("enchanting")
        var enchanting = DMOConfigEnchanting()

        @SerializedName("metalwork")
        var metalwork = DMOConfigMetalwork()

        @SerializedName("cooking")
        var cooking = DMOConfigCooking()
    }

    fun isDiscoveredByDefault(skill: Skill) = !when (skill) {
        LUMBERING -> exp.lumbering.defaultHidden
        MINING -> exp.mining.defaultHidden
        FORAGING -> exp.foraging.defaultHidden
        FISHING -> exp.fishing.defaultHidden
        CULTIVATION -> exp.cultivation.defaultHidden
        ANIMAL_CARE -> exp.animalCare.defaultHidden
        TRADING -> exp.trading.defaultHidden
        DUNGEONEER -> exp.dungeoneer.defaultHidden
        MELEE -> exp.combat.defaultHidden
        RANGER -> exp.combat.defaultHidden
        HUNTER -> exp.hunter.defaultHidden
        ALCHEMY -> exp.alchemy.defaultHidden
        ENCHANTING -> exp.enchanting.defaultHidden
        METALWORK -> exp.metalwork.defaultHidden
        COOKING -> exp.cooking.defaultHidden
        else -> false
    }
}
