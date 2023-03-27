package dev.dakoda.dmo.skills.config

import com.google.gson.annotations.SerializedName

class DMOConfigAnimalCare {

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
            @SerializedName("breeding")
            var breeding: Int = 15

            @SerializedName("taming")
            var taming: Int = 25

            @SerializedName("feeding")
            var feeding: Int = 5
        }
    }
}
