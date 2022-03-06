package dev.dakoda.dmo.skills.client

import dev.dakoda.dmo.skills.ModHelper.buttons
import dev.dakoda.dmo.skills.ModHelper.game
import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.event.PlayerGainEXPCallback
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget
import dev.dakoda.dmo.skills.gui.SkillsScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.ActionResult.PASS
import net.minecraft.util.ActionResult.SUCCESS
import org.lwjgl.glfw.GLFW

class ClientDMOSkills : ClientModInitializer {

    companion object {
        val KEYBINDING_SKILLS_MENU: KeyBinding = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "dmo.skills.keybindings.open_skills_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "dmo.skills.keybindings.category",
            )
        )
    }

    override fun onInitializeClient() {
        registerKeybindings()
        addSkillsButtonToInventory()
        HudRenderCallback.EVENT.register(ClientTrackedSkillHUD.LISTENER)
        PlayerGainEXPCallback.EVENT.register(object : PlayerGainEXPCallback {
            override fun handle(playerEntity: ServerPlayerEntity, increase: Pair<Int, Skill.Sub>): ActionResult {
                if (playerEntity.uuid == game.player?.uuid) {
                    return SUCCESS
                }
                return PASS
            }
        })
    }

    private fun registerKeybindings() {
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (KEYBINDING_SKILLS_MENU.wasPressed()) {
                client.setScreen(SkillsScreen())
            }
        }
    }

    private fun addSkillsButtonToInventory() {
        ScreenEvents.AFTER_INIT.register { client, screen, _, _ ->
            if (screen is InventoryScreen) {
                screen.buttons.add(
                    SkillCategoryWidget.menu(screen) {
                        client?.setScreen(SkillsScreen())
                    }
                )
            }
        }
    }
}
