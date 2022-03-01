package dev.dakoda.dmo.skills.mixin

import dev.dakoda.dmo.skills.PlayerSkill.WOODCUTTING
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMPONENT_PLAYER_SKILLS
import dev.dakoda.dmo.skills.game
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting.*
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(InGameHud::class)
class PMOSkillsHUDMixin {

    @Inject(method = ["render"], at = [At("RETURN")])
    fun onRender(matrixStack: MatrixStack, tickDelta: Float, info: CallbackInfo) {
        val skills = COMPONENT_PLAYER_SKILLS.get(game.player).skills
        val text = TranslatableText("dmo.skills.woodcutting").formatted(ITALIC).append(
            LiteralText(": ")
        ).append(
            LiteralText(skills[WOODCUTTING].toString()).formatted(RESET, WHITE)
        )
        game.textRenderer.draw(matrixStack, text, 5f, 5f, 0xffffff)
    }
}