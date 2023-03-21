package dev.dakoda.dmo.skills.event

import dev.dakoda.dmo.skills.SubSkill
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult

interface PlayerGainEXPCallback {

    companion object {
        val EVENT: Event<PlayerGainEXPCallback> = EventFactory.createArrayBacked(
            PlayerGainEXPCallback::class.java
        ) {
            object : PlayerGainEXPCallback {
                override fun handle(playerEntity: ServerPlayerEntity, increase: Pair<Int, SubSkill>): ActionResult {
                    for (listener in it) {
                        val result = listener.handle(playerEntity, increase)
                        if (result != ActionResult.PASS) return result
                    }

                    return ActionResult.PASS
                }
            }
        }
    }

    fun handle(playerEntity: ServerPlayerEntity, increase: Pair<Int, SubSkill>): ActionResult
}
