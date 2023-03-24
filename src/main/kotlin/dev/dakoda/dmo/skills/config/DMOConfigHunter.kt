package dev.dakoda.dmo.skills.config

import com.google.gson.annotations.SerializedName

class DMOConfigHunter {

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
            @SerializedName("witch")
            var witch: Int = 7

            @SerializedName("elder_guardian")
            var elderGuardian: Int = 12

            @SerializedName("enderman")
            var enderman: Int = 8

            @SerializedName("pillager_patrol_leader")
            var pillagerPatrolLeader: Int = 15

            @SerializedName("evoker")
            var evoker: Int = 20

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
