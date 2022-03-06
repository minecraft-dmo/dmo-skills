package dev.dakoda.dmo.skills.gui

import com.mojang.blaze3d.systems.RenderSystem
import dev.dakoda.dmo.skills.DMOIdentifiers.ICONS_TEXTURE
import dev.dakoda.dmo.skills.ModHelper.game
import dev.dakoda.dmo.skills.ModHelper.leftOfInventory
import dev.dakoda.dmo.skills.ModHelper.topOfInventory
import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.Skills
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMP_SKILLS_TRACKED
import dev.dakoda.dmo.skills.component.SkillsTrackedComponent
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import kotlin.math.roundToInt

private const val BAR_WIDTH = 144

class SkillCategoryContent(
    private val category: Skill,
    private val translationPrefix: String
) : DrawableHelper() {

    private val window get() = game.window

    fun titleX() = window.leftOfInventory.toFloat() + 48f
    fun titleY(index: Int) = window.topOfInventory.toFloat() + 40f + (index * 18f)

    fun getDrawables(skills: Skills) = getSubSkillTexts(skills.subSkills(category)) + getHeader()

    fun getDrawableChildren(skills: Skills): List<ButtonWidget> {
        with(skills.subSkills(category)) {
            return getSubSkillPinToggle(this) + getSubSkillProgressBar(this)
        }
    }

    private fun getHeader(): Drawable {
        return Drawable { matrices, _, _, _ ->
            val headerText = LiteralText("§7--- ").append(TranslatableText("dmo.skills.${category.name.lowercase()}"))
                .append("§7 ---")
            val headerWidth = game.textRenderer.getWidth(headerText)
            val headerX: Float = (window.scaledWidth / 2).toFloat() - (headerWidth / 2)
            val headerY: Float = window.topOfInventory.toFloat() + 20f
            val decorX: Int = (window.scaledWidth / 2) - 73
            val decorY: Int = window.topOfInventory + 4
            RenderSystem.setShaderColor(0.5f, 0.5f, 0.5f, 1f)
            RenderSystem.setShaderTexture(0, ICONS_TEXTURE)
            drawTexture(matrices, decorX, decorY, 0f, 69f, 146, 8, 200, 200)
            drawTexture(matrices, decorX, decorY + 144, 0f, 69f, 146, 8, 200, 200)
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
            game.textRenderer.draw(
                matrices,
                headerText,
                headerX,
                headerY,
                0x000000
            )
        }
    }

    private fun getSubSkillTexts(subSkills: List<Skills.EXP>): List<Drawable> {
        return subSkills.flatMapIndexed { index, exp ->
            listOf(
                // Icon
                Drawable { _, _, _, _ ->
                    game.itemRenderer.renderGuiItemIcon(exp.skill.stack(), titleX().toInt() - 20, titleY(index).toInt())
                },
                // Title
                Drawable { matrices, _, _, _ ->
                    game.textRenderer.draw(
                        matrices,
                        TranslatableText("$translationPrefix.${exp.skill.name.lowercase()}"),
                        titleX(), titleY(index),
                        0x000000
                    )
                }
            )
        }
    }

    private fun getSubSkillPinToggle(subSkills: List<Skills.EXP>): List<ButtonWidget> {
        return subSkills.mapIndexed { index, exp ->
            object : ButtonWidget(
                titleX().toInt() + BAR_WIDTH - 16, titleY(index).toInt() - 2, 12, 12, Text.of(""), {
                    println("Toggling pin for ${exp.skill.name}")
                    (COMP_SKILLS_TRACKED.get(game.player) as SkillsTrackedComponent).toggle(exp.skill as Skill.Sub)
                    COMP_SKILLS_TRACKED.sync(game.player)
                }
            ) {
                override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
                    val isTracked = COMP_SKILLS_TRACKED.get(game.player).trackedSkills[exp.skill] == 1
                    RenderSystem.setShaderTexture(0, ICONS_TEXTURE)
                    val texVModified = if (isTracked) 12f else 0f
                    DrawableHelper.drawTexture(matrices, x, y, 0f, texVModified + 44f, 12, 12, 200, 200)
                    super.render(matrices, mouseX, mouseY, delta)
                }

                override fun renderButton(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
                    // Don't render as a button
                }
            }
        }
    }

    private fun getSubSkillProgressBar(subSkills: List<Skills.EXP>): List<ButtonWidget> {
        return subSkills.mapIndexed { index, exp ->
            val xx = titleX().toInt() - 1
            val yy = titleY(index).toInt() + 9
            object : ButtonWidget(
                xx, yy, BAR_WIDTH, 7, Text.of(""), { }
            ) {

                override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
                    hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height
                    val texVModified = if (hovered) 14f else 0f
                    val expWidth = ((exp.raw.toFloat() / Skills.EXP.perLevel) * BAR_WIDTH.toFloat()).roundToInt()

                    RenderSystem.setShaderTexture(0, ICONS_TEXTURE)
                    DrawableHelper.drawTexture(matrices, xx, yy, 0f, texVModified + 0f, BAR_WIDTH, height, 200, 200)
                    DrawableHelper.drawTexture(matrices, xx, yy, 0f, texVModified + 7f, expWidth, height, 200, 200)

                    if (exp.level > 1) {
                        val text = LiteralText(exp.level.toString())
                        val levelTextX = xx.toFloat() + (BAR_WIDTH / 2) - 2f
                        val levelTextY = yy.toFloat() - 4f
                        game.textRenderer.draw(matrices, text, levelTextX - 1f, levelTextY + 0f, 0x000000)
                        game.textRenderer.draw(matrices, text, levelTextX + 1f, levelTextY + 0f, 0x000000)
                        game.textRenderer.draw(matrices, text, levelTextX + 0f, levelTextY - 1f, 0x000000)
                        game.textRenderer.draw(matrices, text, levelTextX + 0f, levelTextY + 1f, 0x000000)
                        game.textRenderer.draw(matrices, text, levelTextX + 0f, levelTextY + 0f, 0x4ae0f7)
                    }
                    if (isHovered) {
                        game.currentScreen?.renderTooltip(
                            matrices,
                            TranslatableText("dmo.skills.progress", exp.raw, Skills.EXP.perLevel),
                            mouseX, mouseY
                        )
                    }
                }
            }
        }
    }

    companion object {

        fun of(skill: Skill) = SkillCategoryContent(skill, "dmo.skills.${skill.name.lowercase()}")
    }
}
