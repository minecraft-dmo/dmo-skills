package dev.dakoda.dmo.skills.config

import com.google.gson.annotations.SerializedName

class DMOConfigCultivation {

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
            @SerializedName("crops")
            var crops: Int = 6

            @SerializedName("nether_wart")
            var netherWart: Int = 6

            @SerializedName("crops_misc")
            var cropsMisc: Int = 3

            @SerializedName("pumpkin")
            var pumpkin: Int = 6

            @SerializedName("melon")
            var melon: Int = 6
        }
    }
}
