package dev.dakoda.dmo.skills.gui.toast

import com.mojang.blaze3d.systems.RenderSystem
import dev.dakoda.dmo.skills.ModHelper.CONFIG
import dev.dakoda.dmo.skills.ModHelper.game
import dev.dakoda.dmo.skills.Skill
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.toast.Toast
import net.minecraft.client.toast.Toast.Visibility.HIDE
import net.minecraft.client.toast.Toast.Visibility.SHOW
import net.minecraft.client.toast.ToastManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

@Environment(value = EnvType.CLIENT)
class DiscoveryToast(
    private val skill: Skill
) : Toast {

    private val duration = CONFIG.discovery.toastDurationSeconds * 1000L

    private val title = Text.literal("Skill discovered!")
    private val description = Text.literal(skill.name.lowercase().replaceFirstChar { it.titlecaseChar() })

    private var justUpdated: Boolean = true
    private var _startTime: Long = 0

    override fun draw(matrices: MatrixStack, manager: ToastManager, startTime: Long): Toast.Visibility {
        if (justUpdated) {
            _startTime = startTime
            justUpdated = false
        }
        RenderSystem.setShaderTexture(0, Toast.TEXTURE)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        manager.drawTexture(matrices, 0, 0, 0, 96, this.width, this.height)

        game.itemRenderer.renderGuiItemIcon(skill.icon.defaultStack, 6, 6)

        manager.client.textRenderer.draw(matrices, title, 30.0f, 7.0f, -11534256)
        manager.client.textRenderer.draw(matrices, description, 30.0f, 18.0f, -16777216)

        return if (startTime - _startTime < duration) SHOW else HIDE
    }
}
