package dev.dakoda.dmo.skills.config

import com.google.gson.annotations.SerializedName

class DMOConfigEnchanting {

    @SerializedName("enabled")
    var enabled = true

    @SerializedName("default_hidden")
    var defaultHidden = true

    @SerializedName("sources")
    var sources = Sources()

    class Sources {
        @SerializedName("action")
        var action = Action()
        class Action {
            @SerializedName("enchantment_cost_one")
            var enchantmentCostOne: Int = 6

            @SerializedName("enchantment_cost_two")
            var enchantmentCostTwo: Int = 18

            @SerializedName("enchantment_cost_three")
            var enchantmentCostThree: Int = 30
        }
    }
}
