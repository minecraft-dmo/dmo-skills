package dev.dakoda.dmo.skills.gui

import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.Skill.COMBAT
import dev.dakoda.dmo.skills.Skill.CRAFTING
import dev.dakoda.dmo.skills.Skill.EXPLORER
import dev.dakoda.dmo.skills.Skill.FARMING
import dev.dakoda.dmo.skills.Skill.GATHERING
import dev.dakoda.dmo.skills.Skill.MERCHANT
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMPONENT_PLAYER_SKILLS
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget.Companion.BUTTON_SEPARATE
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget.Companion.catCombat
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget.Companion.catCrafting
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget.Companion.catExplorer
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget.Companion.catFarming
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget.Companion.catGathering
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget.Companion.catMerchant
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget.Companion.menu
import io.github.cottonmc.cotton.gui.client.CottonClientScreen
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.util.math.MatrixStack

class SkillsScreen(
    private val client: MinecraftClient?,
    private val screen: Screen?,
) : CottonClientScreen(object : LightweightGuiDescription() {

    private val skills = COMPONENT_PLAYER_SKILLS.get(client?.player).skills

    init {
        val root = WPlainPanel()
        setRootPanel(root)

        root.setSize(176, 166)
        root.insets = Insets.ROOT_PANEL

        root.validate(this)
    }

}) {

    override fun shouldPause() = false

    private val categories = listOf(GATHERING, FARMING, MERCHANT, EXPLORER, COMBAT, CRAFTING)
    private val activeCategory: Skill get() = TrackLastCategory.last

    object TrackLastCategory {
        var last: Skill = GATHERING
    }

    fun swapCategory(skill: Skill) {
        TrackLastCategory.last = skill
    }

    override fun init() {
        super.init()

        addDrawableChild(menu(screen) {
            client?.setScreen(InventoryScreen(client?.player))
        })
        addDrawableChild(catGathering(screen) {
            swapCategory(GATHERING)
        })
        addDrawableChild(catFarming(screen, yOffset = BUTTON_SEPARATE) {
            swapCategory(FARMING)
        })
        addDrawableChild(catMerchant(screen, yOffset = BUTTON_SEPARATE * 2) {
            swapCategory(MERCHANT)
        })
        addDrawableChild(catExplorer(screen, yOffset = BUTTON_SEPARATE * 3) {
            swapCategory(EXPLORER)
        })
        addDrawableChild(catCombat(screen, yOffset = BUTTON_SEPARATE * 4) {
            swapCategory(COMBAT)
        })
        addDrawableChild(catCrafting(screen, yOffset = BUTTON_SEPARATE * 5) {
            swapCategory(CRAFTING)
        })
    }

    override fun keyPressed(ch: Int, keyCode: Int, modifiers: Int): Boolean {
        val wasInventoryKey = client?.options?.keyInventory?.matchesKey(ch, 0) == true
        val wasEscapeKey = ch == 256
        if (wasInventoryKey or wasEscapeKey) this.onClose()
        // Don't forward the inventory button because it will cause the inventory
        // to reappear again.
        return if (!wasInventoryKey) super.keyPressed(ch, keyCode, modifiers) else true
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.render(matrices, mouseX, mouseY, partialTicks)
    }
}
