package dev.dakoda.dmo.skills.client

import dev.dakoda.dmo.skills.ModHelper.buttons
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget
import dev.dakoda.dmo.skills.gui.SkillsScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.minecraft.client.gui.screen.ingame.InventoryScreen

class ClientDMOSkills : ClientModInitializer {

    override fun onInitializeClient() {
        addSkillsButtonToInventory()
    }

    private fun addSkillsButtonToInventory() {
        ScreenEvents.AFTER_INIT.register { client, screen, _, _ ->
            if (screen is InventoryScreen) {
                screen.buttons.add(SkillCategoryWidget.menu(screen) {
                    client?.setScreen(SkillsScreen(client, screen))
                })
            }
        }
    }
}
