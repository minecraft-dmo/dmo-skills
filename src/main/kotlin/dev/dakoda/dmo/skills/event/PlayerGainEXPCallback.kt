package dev.dakoda.dmo.skills.event

import dev.dakoda.dmo.skills.SubSkill
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult

interface PlayerGainEXPCallback {

    companion object {
        val EVENT: Event<PlayerGainEXPCallback> = EventFactory.createArrayBacked(
            PlayerGainEXPCallback::class.java
        ) {
            object : PlayerGainEXPCallback {
                override fun handle(playerEntity: PlayerEntity, gain: Pair<Int, SubSkill>, discovered: Boolean): ActionResult {
                    for (listener in it) {
                        val result = listener.handle(playerEntity, gain, discovered)
                        if (result != ActionResult.PASS) return result
                    }

                    return ActionResult.PASS
                }
            }
        }
    }

    fun handle(playerEntity: PlayerEntity, gain: Pair<Int, SubSkill>, discovered: Boolean): ActionResult
}
