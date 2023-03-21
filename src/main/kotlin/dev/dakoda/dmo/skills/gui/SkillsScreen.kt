package dev.dakoda.dmo.skills.gui

import dev.dakoda.dmo.skills.ModHelper.game
import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.Skill.Companion.GATHERING
import dev.dakoda.dmo.skills.Skill.Companion.NULL
import dev.dakoda.dmo.skills.SkillCategory
import dev.dakoda.dmo.skills.client.ClientDMOSkills.Companion.KEYBINDING_SKILLS_MENU
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMP_SKILLS_EXP
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget.Companion.BUTTON_SEPARATE
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget.Companion.makeCategoryButton
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget.Companion.menuInventory
import io.github.cottonmc.cotton.gui.client.CottonClientScreen
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.util.math.MatrixStack

class SkillsScreen : CottonClientScreen(object : LightweightGuiDescription() {

    init {
        val root = WPlainPanel()
        setRootPanel(root)

        root.setSize(176, 166)
        root.insets = Insets.ROOT_PANEL

        root.validate(this)
    }
}) {

    val skills = COMP_SKILLS_EXP.get(game.player).skills

    override fun shouldPause() = false

    private val categories = Skill.allCategories.filter { it != NULL }

    private val activeCategory: SkillCategory
        get() = TrackLastCategory.last

    private val activeCategoryContent: SkillCategoryContent
        get() = SkillCategoryContent.of(activeCategory)

    object TrackLastCategory {
        var last: SkillCategory = GATHERING
    }

    private fun swapCategory(skill: SkillCategory) {
        if (skill in categories) TrackLastCategory.last = skill
        init()
    }

    override fun init() {
        clearChildren()
        super.init()

        categories.forEachIndexed { index, skill ->
            addDrawableChild(
                makeCategoryButton(this, skill, 0, BUTTON_SEPARATE * index) {
                    swapCategory(skill)
                }.apply {
                    if (activeCategory == skill) isSelected = true
                }
            )
        }
        val content = activeCategoryContent
        content.getDrawables(skills).forEach {
            addDrawable(it)
        }
        content.getDrawableChildren(skills).forEach {
            addDrawableChild(it)
        }
        addDrawableChild(
            menuInventory(this) {
                client?.setScreen(InventoryScreen(client?.player))
            }
        )
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.render(matrices, mouseX, mouseY, partialTicks)
//        SkillCategoryContent.of(activeCategory).render(this, skills, matrices, mouseX, mouseY, partialTicks)
    }

    override fun keyPressed(ch: Int, keyCode: Int, modifiers: Int): Boolean {
        val wasInventoryKey = client?.options?.keyInventory?.matchesKey(ch, 0) == true
        val wasSkillsKey = KEYBINDING_SKILLS_MENU.matchesKey(ch, 0)
        val wasEscapeKey = ch == 256
        if (wasInventoryKey or wasSkillsKey or wasEscapeKey) this.onClose()
        // Don't forward the inventory button because it will cause the inventory
        // to reappear again.
        return if (!wasInventoryKey && !wasSkillsKey) super.keyPressed(ch, keyCode, modifiers) else true
    }
}
