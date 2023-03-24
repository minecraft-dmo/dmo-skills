package dev.dakoda.dmo.skills.config

import com.google.gson.annotations.SerializedName

class DMOConfigCooking {

    @SerializedName("enabled")
    var enabled = true

    @SerializedName("default_hidden")
    var defaultHidden = true

    @SerializedName("sources")
    var sources = Sources()

    class Sources {
        @SerializedName("crafting")
        var crafting = Crafting()

        class Crafting {
            @SerializedName("bread")
            var bread: Int = 2

            @SerializedName("baked_potato")
            var bakedPotato: Int = 5

            @SerializedName("cake")
            var cake: Int = 20

            @SerializedName("cooked_beef")
            var cookedBeef: Int = 5

            @SerializedName("cooked_chicken")
            var cookedChicken: Int = 5

            @SerializedName("cooked_cod")
            var cookedCod: Int = 5

            @SerializedName("cooked_mutton")
            var cookedMutton: Int = 5

            @SerializedName("cooked_porkchop")
            var cookedPorkchop: Int = 5

            @SerializedName("cooked_rabbit")
            var cookedRabbit: Int = 5

            @SerializedName("cooked_salmon")
            var cookedSalmon: Int = 5
        }
    }
}
