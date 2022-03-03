package dev.dakoda.dmo.skills.client

import dev.dakoda.dmo.skills.DMOIdentifiers.Widgets.WIDGETS_TEXTURE
import dev.dakoda.dmo.skills.ModHelper.buttons
import dev.dakoda.dmo.skills.gui.SkillsScreen
import dev.dakoda.dmo.skills.gui.SkillsScreenDescription
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.gui.widget.TexturedButtonWidget

class ClientDMOSkills : ClientModInitializer {

    override fun onInitializeClient() {
        addSkillsButtonToInventory()
    }

    private fun addSkillsButtonToInventory() {
        ScreenEvents.AFTER_INIT.register { client, screen, _, _ ->
            if (screen is InventoryScreen) {
                screen.buttons.add(
                    TexturedButtonWidget(
                        (screen.width / 2) - 110, (screen.height / 2) - 80, 20, 20, 0, 0, 20, WIDGETS_TEXTURE, 20, 40,
                    ) {
                        client.setScreen(SkillsScreen(screen, SkillsScreenDescription(client, screen)))
                    }
                )
            }
        }
    }
}
