package dev.dakoda.dmo.skills.event

import dev.dakoda.dmo.skills.exp.data.EXPGain
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult

interface PlayerGainEXPEvent {

    companion object {
        val EVENT: Event<PlayerGainEXPEvent> = EventFactory.createArrayBacked(
            PlayerGainEXPEvent::class.java
        ) {
            object : PlayerGainEXPEvent {
                override fun handle(playerEntity: PlayerEntity, gain: EXPGain, discovered: Boolean): ActionResult {
                    for (listener in it) {
                        val result = listener.handle(playerEntity, gain, discovered)
                        if (result != ActionResult.PASS) return result
                    }

                    return ActionResult.PASS
                }
            }
        }
    }

    fun handle(playerEntity: PlayerEntity, gain: EXPGain, discovered: Boolean): ActionResult
}
