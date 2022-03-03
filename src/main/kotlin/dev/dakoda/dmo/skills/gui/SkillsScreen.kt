package dev.dakoda.dmo.skills.gui

import dev.dakoda.dmo.skills.DMOIdentifiers.Widgets.WIDGETS_TEXTURE
import dev.dakoda.dmo.skills.ModHelper.buttons
import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMPONENT_PLAYER_SKILLS
import io.github.cottonmc.cotton.gui.client.CottonClientScreen
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.gui.widget.TexturedButtonWidget

class SkillsScreenDescription(
    val client: MinecraftClient,
    val screen: Screen
) : LightweightGuiDescription() {

    private val skills = COMPONENT_PLAYER_SKILLS.get(client.player).skills

    init {
        val root = WPlainPanel()
        setRootPanel(root)

        root.setSize(176, 166)
        root.insets = Insets.ROOT_PANEL

        root.validate(this)

        println(skills.progressOf(Skill.Sub.LUMBERING).raw.toString())
    }
}

class SkillsScreen(
    private val screen: Screen,
    skillsScreenDescription: SkillsScreenDescription,
) : CottonClientScreen(skillsScreenDescription) {

    override fun shouldPause() = false

    override fun init() {
        super.init()
        buttons.add(
            TexturedButtonWidget(
                (screen.width / 2) - 110, (screen.height / 2) - 80, 20, 20, 0, 0, 20, WIDGETS_TEXTURE, 20, 40,
            ) {
                client?.setScreen(InventoryScreen(client?.player))
            }
        )
    }

    override fun keyPressed(ch: Int, keyCode: Int, modifiers: Int): Boolean {
        val wasInventoryKey = client?.options?.keyInventory?.matchesKey(ch, 0) == true
        val wasEscapeKey = ch == 256
        if (wasInventoryKey or wasEscapeKey) this.onClose()
        // Don't forward the inventory button because it will cause the inventory
        // to reappear again.
        return if (!wasInventoryKey) super.keyPressed(ch, keyCode, modifiers) else true
    }
}
