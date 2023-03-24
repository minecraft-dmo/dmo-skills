package dev.dakoda.dmo.skills.config

import com.google.gson.annotations.SerializedName

class DMOConfigFishing {

    @SerializedName("enabled")
    var enabled = true

    @SerializedName("default_hidden")
    var defaultHidden = true

    @SerializedName("sources")
    var sources = Sources()

    class Sources {
        @SerializedName("catching")
        var catching = Catching()

        class Catching {
            @SerializedName("lily_pad")
            var lilyPad: Int = 4

            @SerializedName("bowl")
            var bowl: Int = 4

            @SerializedName("leather")
            var leather: Int = 4

            @SerializedName("rotten_flesh")
            var rottenFlesh: Int = 4

            @SerializedName("stick")
            var stick: Int = 4

            @SerializedName("string")
            var string: Int = 4

            @SerializedName("potion")
            var potion: Int = 4

            @SerializedName("bone")
            var bone: Int = 4

            @SerializedName("ink_sac")
            var inkSac: Int = 4

            @SerializedName("tripwire_hook")
            var tripwireHook: Int = 4

            @SerializedName("bamboo")
            var bamboo: Int = 4

            @SerializedName("leather_boots")
            var leatherBoots: Int = 6

            @SerializedName("cod_salmon")
            var codSalmon: Int = 10

            @SerializedName("pufferfish")
            var pufferFish: Int = 15

            @SerializedName("tropical_fish")
            var tropicalFish: Int = 20

            @SerializedName("fish_misc")
            var fishMisc: Int = 10

            @SerializedName("bow")
            var bow: Int = 5

            @SerializedName("fishing_rod")
            var fishingRod: Int = 5

            @SerializedName("enchanted_catch_multiplier")
            var enchantedMultiplier: Int = 6

            @SerializedName("enchanted_book")
            var enchantedBook: Int = 30

            @SerializedName("name_tag")
            var nameTag: Int = 30

            @SerializedName("nautilus_shell")
            var nautilusShell: Int = 30

            @SerializedName("saddle")
            var saddle: Int = 30
        }
    }
}
