package dev.dakoda.dmo.skills.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.advancement.Advancement
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult

interface PlayerAdvancementEvent {

    companion object {
        val EVENT: Event<PlayerAdvancementEvent> = EventFactory.createArrayBacked(
            PlayerAdvancementEvent::class.java
        ) {
            object : PlayerAdvancementEvent {
                override fun handle(playerEntity: PlayerEntity, advancement: Advancement): ActionResult {
                    for (listener in it) {
                        val result = listener.handle(playerEntity, advancement)
                        if (result != ActionResult.PASS) return result
                    }

                    return ActionResult.PASS
                }
            }
        }
    }

    fun handle(playerEntity: PlayerEntity, advancement: Advancement): ActionResult
}
