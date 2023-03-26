package dev.dakoda.dmo.skills.config

import com.google.gson.annotations.SerializedName

class DMOConfigTrading {

    @SerializedName("enabled")
    var enabled = true

    @SerializedName("default_hidden")
    var defaultHidden = true

    @SerializedName("sources")
    var sources = Sources()

    class Sources {
        @SerializedName("trading")
        var trading = Trading()

        class Trading {
            @SerializedName("exp_per_item")
            var expPerItem: Float = 0.5f

            @SerializedName("exp_per_emerald")
            var expPerEmerald: Float = 4f

            @SerializedName("novice_multiplier")
            var noviceMultiplier: Float = 1.00f

            @SerializedName("apprentice_multiplier")
            var apprenticeMultiplier: Float = 1.08f

            @SerializedName("journeyman_multiplier")
            var journeymanMultiplier: Float = 1.16f

            @SerializedName("expert_multiplier")
            var expertMultiplier: Float = 1.24f

            @SerializedName("master_multiplier")
            var masterMultiplier: Float = 1.32f
        }
    }
}
