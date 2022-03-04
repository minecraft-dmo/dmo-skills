package dev.dakoda.dmo.skills.client

import com.mojang.blaze3d.systems.RenderSystem
import dev.dakoda.dmo.skills.DMOIdentifiers.ICONS_TEXTURE
import dev.dakoda.dmo.skills.ModHelper.game
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMP_SKILLS_EXP
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMP_SKILLS_TRACKED
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.gui.DrawableHelper
import kotlin.math.roundToInt

object ClientTrackedSkillHUD : DrawableHelper() {

    val LISTENER: HudRenderCallback = HudRenderCallback { matrixStack, _ ->
        val skills = COMP_SKILLS_EXP.get(game.player).skills
        val tracked = COMP_SKILLS_TRACKED.get(game.player).trackedSkills.filter { it.value == 1 }
        val toTrack = skills.subSkills().filter { it.skill in tracked.keys }

        val trackerStartX = 12
        val trackerStartY = 12
        val trackerDivider = 16
        toTrack.forEachIndexed { index, exp ->
            val y = trackerStartY + (index * trackerDivider)
            val barWidth = 62
            val expWidth = ((exp.raw.toFloat() / exp.perLevel.toFloat()) * barWidth.toFloat()).roundToInt()
            RenderSystem.setShaderTexture(0, ICONS_TEXTURE)
            drawTexture(matrixStack, trackerStartX + 10, y - 2, 1f, 29f, barWidth, 5, 200, 200)
            drawTexture(matrixStack, trackerStartX + 10, y - 2, 1f, 34f, expWidth, 5, 200, 200)

            game.itemRenderer.renderGuiItemIcon(exp.skill.stack(), trackerStartX - 8, y - 8)
        }
    }
}
