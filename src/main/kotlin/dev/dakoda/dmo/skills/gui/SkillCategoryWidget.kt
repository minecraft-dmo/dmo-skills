package dev.dakoda.dmo.skills.gui

import com.mojang.blaze3d.systems.RenderSystem
import dev.dakoda.dmo.skills.DMOIdentifiers
import dev.dakoda.dmo.skills.ModHelper.game
import dev.dakoda.dmo.skills.ModHelper.leftOfInventory
import dev.dakoda.dmo.skills.ModHelper.rightOfInventory
import dev.dakoda.dmo.skills.ModHelper.topOfInventory
import dev.dakoda.dmo.skills.SkillCategory
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget.PressAction
import net.minecraft.client.gui.widget.TexturedButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.item.Items.CHEST
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier

class SkillCategoryWidget(
    pressAction: PressAction = PressAction { },
    x: Int = 0,
    y: Int = 0,
    width: Int = 20,
    height: Int = 20,
    var texU: Int = 0,
    var texV: Int = 0,
    var hoveredVOffset: Int = height,
    var tex: Identifier = DMOIdentifiers.WIDGETS_TEXTURE,
    texW: Int = width,
    texH: Int = height * 2,
) : TexturedButtonWidget(x, y, width, height, texU, texV, hoveredVOffset, tex, texW, texH, pressAction) {

    private val window get() = game.window

    var isSelected: Boolean = false

    private val texVModified get() = if (this.isHovered or this.isSelected) texV + hoveredVOffset else texV

    lateinit var renderOverride: (matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) -> Unit

    override fun renderButton(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        if (this::renderOverride.isInitialized) {
            renderOverride(matrices, mouseX, mouseY, delta)
        } else super.renderButton(matrices, mouseX, mouseY, delta)
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(matrices, mouseX, mouseY, delta)
    }

    companion object {

        private const val U_BLANK_BUTTON = 0
        private const val U_BOOK_BUTTON = 20

        const val BUTTON_SIZE = 20
        const val BUTTON_SEPARATE = BUTTON_SIZE + 2

        fun menu(
            screen: Screen?,
            xOffset: Int = 0,
            yOffset: Int = 0,
            pressAction: PressAction
        ) = SkillCategoryWidget(pressAction).apply {
            renderOverride = { matrices, mouseX, mouseY, _ ->
                RenderSystem.setShaderTexture(0, tex)
                drawTexture(matrices, x, y, texU.float, texVModified.float, width, height, 100, 100)

                if (isHovered) {
                    screen?.renderTooltip(matrices, TranslatableText("dmo.skills"), mouseX, mouseY)
                }
            }
            x = window.leftOfInventory + xOffset; y = window.topOfInventory + yOffset
            texU = U_BOOK_BUTTON
        }

        fun menuInventory(
            screen: Screen?,
            xOffset: Int = 0,
            yOffset: Int = 0,
            pressAction: PressAction
        ) = SkillCategoryWidget(pressAction).apply {
            renderOverride = { matrices, mouseX, mouseY, _ ->
                RenderSystem.setShaderTexture(0, tex)
                drawTexture(matrices, x, y, texU.float, texVModified.float, width, height, 100, 100)

                game.itemRenderer.renderGuiItemIcon(CHEST.defaultStack, x + 2, y + 1)
                if (isHovered) {
                    screen?.renderTooltip(matrices, TranslatableText("dmo.skills.inventory.back"), mouseX, mouseY)
                }
            }
            x = window.leftOfInventory + xOffset; y = window.topOfInventory + yOffset
            texU = U_BLANK_BUTTON
        }

        private fun categoryButton(
            screen: Screen?,
            itemIcon: ItemStack,
            toolTipKey: String,
            pressAction: PressAction
        ) = SkillCategoryWidget(pressAction).apply {
            renderOverride = { matrices, mouseX, mouseY, _ ->
                // Draw button background
                RenderSystem.setShaderTexture(0, tex)
                drawTexture(matrices, x, y, texU.float, texVModified.float, width, height, 100, 100)

                // Draw button icon
                game.itemRenderer.renderGuiItemIcon(itemIcon, x + 2, y + 2)

                if (isHovered) {
                    screen?.renderTooltip(matrices, TranslatableText(toolTipKey), mouseX, mouseY)
                }
            }
        }

        fun makeCategoryButton(
            screen: Screen?,
            skill: SkillCategory,
            xOffset: Int = 0,
            yOffset: Int = 0,
            pressAction: PressAction,
        ) = categoryButton(
            screen,
            skill.stack(), "dmo.skills.${skill.name.lowercase()}",
            pressAction
        ).apply {
            x = window.rightOfInventory + xOffset; y = window.topOfInventory + yOffset
            texU = U_BLANK_BUTTON
        }

        private val Int.float get() = this.toFloat()
    }
}
