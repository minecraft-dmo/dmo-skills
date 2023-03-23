package dev.dakoda.dmo.skills.config.dungeoneer

import com.google.gson.annotations.SerializedName

class DMOConfigDungeoneer {

    @SerializedName("enabled") var enabled = true
    @SerializedName("default_hidden") var defaultHidden = true
    @SerializedName("sources") var sources = Sources()

    class Sources {
        @SerializedName("break") var break_ = BreakBlock()
        @SerializedName("use") var use = UseBlock()

        class BreakBlock {
            @SerializedName("chest") var chest: Int = 20
            @SerializedName("minecart_chest") var minecartChest: Int = 20
        }

        class UseBlock {
            @SerializedName("chest") var chest: Int = 20
            @SerializedName("minecart_chest") var minecartChest: Int = 20
        }
    }
}