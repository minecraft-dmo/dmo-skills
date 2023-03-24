package dev.dakoda.dmo.skills.config

import com.google.gson.annotations.SerializedName

class DMOConfigMining {

    @SerializedName("enabled")
    var enabled = true

    @SerializedName("default_hidden")
    var defaultHidden = true

    @SerializedName("sources")
    var sources = Sources()

    class Sources {
        @SerializedName("break")
        var break_ = BreakBlock()

        class BreakBlock {
            @SerializedName("coal_ores")
            var coalOres: Int = 1

            @SerializedName("copper_ores")
            var copperOres: Int = 2

            @SerializedName("iron_ores")
            var ironOres: Int = 3

            @SerializedName("lapis_ores")
            var lapisOres: Int = 5

            @SerializedName("gold_ores")
            var goldOres: Int = 5

            @SerializedName("gold_ore_nether")
            var netherGoldOre: Int = 1

            @SerializedName("diamond_ores")
            var diamondOres: Int = 8

            @SerializedName("emerald_ores")
            var emeraldOres: Int = 2

            @SerializedName("ancient_debris")
            var ancientDebris: Int = 2

            @SerializedName("amethyst_cluster")
            var amethystCluster: Int = 2
        }
    }
}
