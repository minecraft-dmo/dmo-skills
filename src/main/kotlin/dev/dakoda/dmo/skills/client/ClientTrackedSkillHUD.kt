package dev.dakoda.dmo.skills.client

import com.mojang.blaze3d.systems.RenderSystem
import dev.dakoda.dmo.skills.DMOIdentifiers.ICONS_TEXTURE
import dev.dakoda.dmo.skills.ModHelper.game
import dev.dakoda.dmo.skills.Skill
import dev.dakoda.dmo.skills.Skills.EXP.Companion.perLevel
import dev.dakoda.dmo.skills.SubSkill
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMP_SKILLS_EXP
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMP_SKILLS_TRACKED
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.text.Text
import kotlin.math.roundToInt
import kotlin.math.roundToLong

object ClientTrackedSkillHUD : DrawableHelper() {

    private var olds: MutableMap<SubSkill, Long>? = null

    private fun SubSkill.old() = olds!![this]

    val LISTENER: HudRenderCallback = HudRenderCallback { matrices, tickDelta ->
        if (olds == null) {
            val skills = COMP_SKILLS_EXP.get(game.player).skills
            olds = Skill.allSubSkills.associateWith {
                skills.progressOf(it).raw
            }.toMutableMap()
        }

        val skills = COMP_SKILLS_EXP.get(game.player).skills
        val tracked = COMP_SKILLS_TRACKED.get(game.player).trackedSkills.filter { it.value == 1 }
        val toTrack = skills.subSkills().filter { it.skill in tracked.keys }

        val trackerStartX = 12
        val trackerStartY = 12
        val trackerDivider = 16
        toTrack.forEachIndexed { index, exp ->
            if (exp.skill is SubSkill) {
                val y = trackerStartY + (index * trackerDivider)
                val barWidth = 62
                val expWidth = ((exp.raw.toFloat() / perLevel) * barWidth.toFloat()).roundToInt()
                RenderSystem.setShaderTexture(0, ICONS_TEXTURE)
                val xx = trackerStartX + 10
                val yy = y - 2
                drawTexture(matrices, xx, yy, 1f, 29f, barWidth, 5, 200, 200)

                game.itemRenderer.renderGuiItemIcon(exp.skill.stack(), trackerStartX - 8, y - 8)

                if ((exp.skill in olds!!)) {
                    var old = exp.skill.old() ?: 0L
                    if (exp.raw < old) {
                        old = 0L
                    }

                    if (old < exp.raw) {
                        if (old + tickDelta > exp.raw) {
                            old = exp.raw
                        } else {
                            old += tickDelta.roundToLong()
                        }

//                        println(old)

                        val oldExpWidth =
                            ((old.toDouble() / perLevel) * barWidth.toDouble()).roundToInt()
                        RenderSystem.setShaderTexture(0, ICONS_TEXTURE)
                        drawTexture(matrices, xx, yy, 1f, 39f, expWidth, 5, 200, 200)
                        drawTexture(matrices, xx, yy, 1f, 34f, oldExpWidth, 5, 200, 200)
                    } else if (old == exp.raw) {
                        RenderSystem.setShaderTexture(0, ICONS_TEXTURE)
                        drawTexture(matrices, xx, yy, 1f, 34f, expWidth, 5, 200, 200)
                    }
                    olds!![exp.skill] = old
                }

                if (exp.level > 1) {
                    val text = Text.literal(exp.level.toString())
                    val textWidth = game.textRenderer.getWidth(text)
                    val levelTextX = xx.toFloat() + (barWidth / 2) - (textWidth / 2)
                    val levelTextY = yy.toFloat() - 4f
                    game.textRenderer.draw(matrices, text, levelTextX - 1f, levelTextY + 0f, 0x000000)
                    game.textRenderer.draw(matrices, text, levelTextX + 1f, levelTextY + 0f, 0x000000)
                    game.textRenderer.draw(matrices, text, levelTextX + 0f, levelTextY - 1f, 0x000000)
                    game.textRenderer.draw(matrices, text, levelTextX + 0f, levelTextY + 1f, 0x000000)
                    game.textRenderer.draw(matrices, text, levelTextX + 0f, levelTextY + 0f, 0x4ae0f7)
                }
            }
        }
    }
}
