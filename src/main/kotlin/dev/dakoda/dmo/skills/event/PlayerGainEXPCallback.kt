package dev.dakoda.dmo.skills.event

import dev.dakoda.dmo.skills.SubSkill
import dev.dakoda.dmo.skills.exp.EXPGain
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
                override fun handle(playerEntity: PlayerEntity, gain: EXPGain, discovered: Boolean): ActionResult {
                    for (listener in it) {
                        val result = listener.handle(playerEntity, gain, discovered)
                        if (result != ActionResult.PASS) return result
                    }

                    return ActionResult.PASS
                }

                override fun handle(playerEntity: PlayerEntity, gain: Pair<Int, SubSkill>, discovered: Boolean): ActionResult {
                    return handle(playerEntity, EXPGain(gain), discovered)
                }
            }
        }
    }

    fun handle(playerEntity: PlayerEntity, gain: Pair<Int, SubSkill>, discovered: Boolean): ActionResult
    fun handle(playerEntity: PlayerEntity, gain: EXPGain, discovered: Boolean): ActionResult
}
