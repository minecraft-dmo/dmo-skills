package dev.dakoda.dmo.skills.gui

import com.mojang.blaze3d.systems.RenderSystem
import dev.dakoda.dmo.skills.ModHelper.game
import dev.dakoda.dmo.skills.ModHelper.resource
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget.PressAction
import net.minecraft.client.gui.widget.TexturedButtonWidget
import net.minecraft.client.util.Window
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.item.Items.COMPASS
import net.minecraft.item.Items.EMERALD
import net.minecraft.item.Items.IRON_INGOT
import net.minecraft.item.Items.IRON_ORE
import net.minecraft.item.Items.IRON_SWORD
import net.minecraft.item.Items.POTION
import net.minecraft.item.Items.RAW_IRON
import net.minecraft.item.Items.SWEET_BERRIES
import net.minecraft.item.Items.WHEAT
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier

class SkillCategoryWidget(
    screen: Screen?,
    pressAction: PressAction = PressAction { },
    x: Int = 0, y: Int = 0,
    width: Int = 20, height: Int = 20,
    override var texU: Int = 0, override var texV: Int = 0,
    override var hoveredVOffset: Int = height,
    var tex: Identifier = resource("textures/gui/widgets.png"),
    override var texW: Int = width, override var texH: Int = height * 2,
) : TexturedButtonWidget(x, y, width, height, texU, texV, hoveredVOffset, tex, texW, texH, pressAction), DMOWidget {

    private val window get() = game.window

    private val texVModified get() = if (this.isHovered) texV + hoveredVOffset else texV

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
        const val BUTTON_SEPARATE = 22

        fun menu(
            screen: Screen?,
            xOffset: Int = 0, yOffset: Int = 0,
            pressAction: PressAction
        ) = SkillCategoryWidget(screen, pressAction).apply {
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

        fun categoryButton(
            screen: Screen?,
            itemIcon: ItemStack, toolTipKey: String,
            xOffset: Int = 0, yOffset: Int = 0,
            pressAction: PressAction
        ) = SkillCategoryWidget(screen, pressAction).apply {
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

        fun catGathering(
            screen: Screen?,
            xOffset: Int = 0, yOffset: Int = 0, pressAction: PressAction
        ) = categoryButton(
            screen,
            RAW_IRON.defaultStack, "dmo.skills.gathering",
            xOffset, yOffset, pressAction
        ).apply {
            x = window.rightOfInventory + xOffset; y = window.topOfInventory + yOffset
            texU = U_BLANK_BUTTON
        }

        fun catFarming(
            screen: Screen?,
            xOffset: Int = 0, yOffset: Int = 0, pressAction: PressAction
        ) = categoryButton(
            screen,
            WHEAT.defaultStack, "dmo.skills.farming",
            xOffset, yOffset, pressAction
        ).apply {
            x = window.rightOfInventory + xOffset; y = window.topOfInventory + yOffset
            texU = U_BLANK_BUTTON
        }

        fun catMerchant(
            screen: Screen?,
            xOffset: Int = 0, yOffset: Int = 0, pressAction: PressAction
        ) = categoryButton(
            screen,
            EMERALD.defaultStack, "dmo.skills.merchant",
            xOffset, yOffset, pressAction
        ).apply {
            x = window.rightOfInventory + xOffset; y = window.topOfInventory + yOffset
            texU = U_BLANK_BUTTON
        }

        fun catExplorer(
            screen: Screen?,
            xOffset: Int = 0, yOffset: Int = 0, pressAction: PressAction
        ) = categoryButton(
            screen,
            COMPASS.defaultStack, "dmo.skills.explorer",
            xOffset, yOffset, pressAction
        ).apply {
            x = window.rightOfInventory + xOffset; y = window.topOfInventory + yOffset
            texU = U_BLANK_BUTTON
        }

        fun catCombat(
            screen: Screen?,
            xOffset: Int = 0, yOffset: Int = 0, pressAction: PressAction
        ) = categoryButton(
            screen,
            IRON_SWORD.defaultStack, "dmo.skills.combat",
            xOffset, yOffset, pressAction
        ).apply {
            x = window.rightOfInventory + xOffset; y = window.topOfInventory + yOffset
            texU = U_BLANK_BUTTON
        }

        fun catCrafting(
            screen: Screen?,
            xOffset: Int = 0, yOffset: Int = 0, pressAction: PressAction
        ) = categoryButton(
            screen,
            POTION.defaultStack, "dmo.skills.crafting",
            xOffset, yOffset, pressAction
        ).apply {
            x = window.rightOfInventory + xOffset; y = window.topOfInventory + yOffset
            texU = U_BLANK_BUTTON
        }

        private val Window.leftOfInventory get() = (this.scaledWidth / 2) - 110
        private val Window.rightOfInventory get() = (this.scaledWidth / 2) + 90
        private val Window.topOfInventory get() = (this.scaledHeight / 2) - 80

        private val Int.float get() = this.toFloat()
    }
}
