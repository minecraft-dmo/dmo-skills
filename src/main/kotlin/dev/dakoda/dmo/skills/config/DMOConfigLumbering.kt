package dev.dakoda.dmo.skills.config

import com.google.gson.annotations.SerializedName

class DMOConfigLumbering {

    @SerializedName("enabled")
    var enabled = true

    @SerializedName("default_hidden")
    var defaultHidden = true

    @SerializedName("sources")
    var sources = Sources()

    class Sources {
        @SerializedName("break")
        var break_ = BreakBlock()

        @SerializedName("use")
        var use = UseBlock()

        class BreakBlock {
            @SerializedName("logs")
            var logs: Int = 2

            @SerializedName("mushroom_blocks")
            var mushroomBlocks: Int = 2
        }

        class UseBlock {
            @SerializedName("strip_logs")
            var stripLogs: Int = 5
        }
    }
}
