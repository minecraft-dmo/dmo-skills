package dev.dakoda.dmo.skills.config

import com.google.gson.annotations.SerializedName

class DMOConfigMetalwork {

    @SerializedName("enabled")
    var enabled = true

    @SerializedName("default_hidden")
    var defaultHidden = true

    @SerializedName("sources")
    var sources = Sources()

    class Sources {
        @SerializedName("brewing")
        var brewing = Brewing()

        @SerializedName("action")
        var action = Action()

        class Brewing {
            @SerializedName("awkward")
            var awkward: Int = 4

            @SerializedName("mundane")
            var mundane: Int = 1

            @SerializedName("thick")
            var thick: Int = 1

            @SerializedName("base_effects")
            var standardEffects: Int = 8

            @SerializedName("extended_duration")
            var extendedDuration: Int = 6

            @SerializedName("increased_power")
            var increasedPower: Int = 10
        }

        class Action {
            @SerializedName("grindstone_repair_minimum_damage")
            var grindstoneRepairMinimumDamage: Float = 0.1f

            @SerializedName("grindstone_repair_maximum")
            var grindstoneRepairMaximum: Int = 30

            @SerializedName("grindstone_disenchant")
            var grindstoneDisenchant: Int = 6
        }
    }
}
