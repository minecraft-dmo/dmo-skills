package dev.dakoda.dmo.skills.config

import com.google.gson.annotations.SerializedName

class DMOConfigCombat {

    @SerializedName("enabled")
    var enabled = true

    @SerializedName("default_hidden")
    var defaultHidden = true

    @SerializedName("sources")
    var sources = Sources()

    class Sources {
        @SerializedName("killing")
        var killing = Killing()

        class Killing {
            @SerializedName("endermite")
            var endermite: Int = 1

            @SerializedName("silverfish")
            var silverfish: Int = 1

            @SerializedName("piglin")
            var piglin: Int = 1

            @SerializedName("wolf_angry_at_you")
            var wolfAngryAtYou: Int = 4

            @SerializedName("wolf_angry")
            var wolfAngry: Int = 3

            @SerializedName("wolf")
            var wolf: Int = 2

            @SerializedName("slime_big")
            var slimeBig: Int = 4

            @SerializedName("slime_medium")
            var slimeMedium: Int = 3

            @SerializedName("slime_small")
            var slimeSmall: Int = 2

            @SerializedName("slime_tiny")
            var slimeTiny: Int = 1

            @SerializedName("spider")
            var spider: Int = 4

            @SerializedName("cave_spider")
            var caveSpider: Int = 4

            @SerializedName("zombie")
            var zombie: Int = 5

            @SerializedName("creeper")
            var creeper: Int = 5

            @SerializedName("shulker")
            var shulker: Int = 5

            @SerializedName("skeleton")
            var skeleton: Int = 7

            @SerializedName("witch")
            var witch: Int = 7

            @SerializedName("hoglin")
            var hoglin: Int = 7

            @SerializedName("zoglin")
            var zoglin: Int = 7

            @SerializedName("elder_guardian")
            var elderGuardian: Int = 12

            @SerializedName("enderman")
            var enderman: Int = 8

            @SerializedName("guardian")
            var guardian: Int = 8

            @SerializedName("pillagers")
            var pillagers: Int = 6

            @SerializedName("phantom")
            var phantom: Int = 1

            @SerializedName("vex")
            var vex: Int = 6

            @SerializedName("ravager")
            var ravager: Int = 10

            @SerializedName("wither_skeleton")
            var witherSkeleton: Int = 10

            @SerializedName("warden")
            var warden: Int = 45

            @SerializedName("wither")
            var wither: Int = 60

            @SerializedName("ender_dragon")
            var enderDragon: Int = 120
        }
    }
}
