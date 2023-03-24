package dev.dakoda.dmo.skills.gui

import com.mojang.blaze3d.systems.RenderSystem
import dev.dakoda.dmo.skills.DMOIdentifiers
import dev.dakoda.dmo.skills.DMOSkills
import dev.dakoda.dmo.skills.DMOSkills.game
import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.Skill.Companion.NULL_CAT
import dev.dakoda.dmo.skills.client.ClientModInitialiser.Companion.KEYBINDING_SKILLS_MENU
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMP_SKILLS_DISCOVERED
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMP_SKILLS_EXP
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget.Companion.BUTTON_SEPARATE
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget.Companion.makeCategoryButton
import dev.dakoda.dmo.skills.gui.SkillCategoryWidget.Companion.menuInventory
import io.github.cottonmc.cotton.gui.client.CottonClientScreen
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.client.gui.Drawable
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

    val skills = COMP_SKILLS_EXP.get(game.player!!).skills
    private val discoveries = COMP_SKILLS_DISCOVERED.get(game.player!!).skillsDiscovered

    override fun shouldPause() = false

    private val categories = Skill.allCategories.filter { it != NULL_CAT }

    private val activeCategory: Skill.Category
        get() = TrackLastCategory.last

    private val activeCategoryContent: SkillCategoryContent
        get() = SkillCategoryContent.of(activeCategory)

    object TrackLastCategory {
        var set: Boolean = false
        lateinit var last: Skill.Category
    }

    private fun swapCategory(skill: Skill.Category) {
        if (skill in categories) TrackLastCategory.last = skill
        init()
    }

    override fun init() {
        clearChildren()
        super.init()

        categories.filter { it.subSkills.any { discoveries[it] ?: DMOSkills.CONFIG.isDiscoveredByDefault(it) } }.forEachIndexed { index, skillCategory ->
            if (!TrackLastCategory.set) {
                TrackLastCategory.set = true
                TrackLastCategory.last = skillCategory
            }
            addDrawableChild(
                makeCategoryButton(this, skillCategory, 0, BUTTON_SEPARATE * index) {
                    swapCategory(skillCategory)
                }.apply {
                    if (activeCategory == skillCategory) isSelected = true
                }
            )
        }
        if (!TrackLastCategory.set) {
            addDrawable(blankPage())
        } else {
            val content = activeCategoryContent
            content.getDrawables(skills, discoveries).forEach { addDrawable(it) }
            content.getDrawableChildren(skills, discoveries).forEach { addDrawableChild(it) }
        }
        addDrawableChild(
            menuInventory(this) {
                client?.setScreen(InventoryScreen(client?.player))
            }
        )
    }

    fun blankPage(): Drawable {
        return Drawable { matrices, _, _, _ ->
            windowDecor(matrices)
        }
    }

    companion object {
        fun windowDecor(matrices: MatrixStack) {
            val decorX: Int = (game.window.scaledWidth / 2) - 73
            val decorY: Int = game.window.topOfInventory + 4
            RenderSystem.setShaderColor(0.5f, 0.5f, 0.5f, 1f)
            RenderSystem.setShaderTexture(0, DMOIdentifiers.ICONS_TEXTURE)
            drawTexture(matrices, decorX, decorY, 0f, 69f, 146, 8, 200, 200)
            drawTexture(matrices, decorX, decorY + 144, 0f, 69f, 146, 8, 200, 200)
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        }
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.render(matrices, mouseX, mouseY, partialTicks)
//        SkillCategoryContent.of(activeCategory).render(this, skills, matrices, mouseX, mouseY, partialTicks)
    }

    override fun keyPressed(ch: Int, keyCode: Int, modifiers: Int): Boolean {
        val wasInventoryKey = client?.options?.inventoryKey?.matchesKey(ch, 0) == true
        val wasSkillsKey = KEYBINDING_SKILLS_MENU.matchesKey(ch, 0)
        val wasEscapeKey = ch == 256
        if (wasInventoryKey or wasSkillsKey or wasEscapeKey) this.close()
        // Don't forward the inventory button because it will cause the inventory
        // to reappear again.
        return if (!wasInventoryKey && !wasSkillsKey) super.keyPressed(ch, keyCode, modifiers) else true
    }
}
