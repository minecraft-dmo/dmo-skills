package dev.dakoda.dmo.skills.config

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DMOConfigCooking : OverridableConfig {

    @SerializedName("enabled")
    var enabled = true

    @SerializedName("default_hidden")
    var defaultHidden = true

    @Expose(serialize = false, deserialize = false)
    override var overridden = false

    @Suppress("unused")
    @SerializedName("warning")
    var warning = "The Cooking skill of this base mod can be overwritten by dmo-cooking, in which case this config section will not take effect."

    @SerializedName("sources")
    var sources = Sources()

    class Sources {
        @SerializedName("crafting")
        var crafting = Crafting()

        class Crafting {
            @SerializedName("bread")
            var bread: Int = 2

            @SerializedName("cookies")
            var cookies: Int = 4

            @SerializedName("baked_potato")
            var bakedPotato: Int = 5

            @SerializedName("pumpkin_pie")
            var pumpkinPie: Int = 8

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
