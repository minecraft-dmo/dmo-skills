package dev.dakoda.dmo.skills

import dev.dakoda.dmo.skills.PlayerSkill.WOODCUTTING
import dev.dakoda.dmo.skills.component.DMOSkillsComponents
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.particle.DamageParticle
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.ParticleUtil
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.particle.ParticleTypes.DAMAGE_INDICATOR
import net.minecraft.tag.BlockTags
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.DyeColor
import net.minecraft.util.Formatting
import net.minecraft.util.Util
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Matrix4f

class DMOSkills : ModInitializer {

    override fun onInitialize() {
        PlayerBlockBreakEvents.AFTER.register { world, player, blockPos, blockState, blockEntity ->
            val playerSkills = DMOSkillsComponents.COMPONENT_PLAYER_SKILLS.get(player).skills
            if (blockState.block in BlockTags.LOGS) {
                playerSkills.increment(0.1f, WOODCUTTING)
                player.sendSystemMessage(LiteralText("Woodcutting: ${playerSkills[WOODCUTTING]}"), Util.NIL_UUID)
            }

//            HudRenderCallback.EVENT.register { matrices, delta ->
//                MinecraftClient.getInstance().textRenderer.draw(matrices, "hello", 5f, 5f, 0)
//
//            }

            // Text text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light
        }
    }
}