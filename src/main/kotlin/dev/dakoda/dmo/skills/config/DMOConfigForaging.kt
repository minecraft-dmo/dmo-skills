package dev.dakoda.dmo.skills.config

import com.google.gson.annotations.SerializedName

class DMOConfigForaging {

    @SerializedName("enabled")
    var enabled = true

    @SerializedName("default_hidden")
    var defaultHidden = true

    @SerializedName("sources")
    var sources = Sources()

    class Sources {
        @SerializedName("harvest")
        var harvest = HarvestBlock()

        class HarvestBlock {
            @SerializedName("sweet_berries_few")
            var sweetBerriesFew: Int = 3

            @SerializedName("sweet_berries_many")
            var sweetBerriesMany: Int = 5

            @SerializedName("glow_berries")
            var glowBerries: Int = 5
        }
    }
}
