package dev.dakoda.dmo.skills

import dev.dakoda.dmo.skills.Skill.Sub.LUMBERING
import dev.dakoda.dmo.skills.component.DMOSkillsComponents
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.tag.BlockTags
import net.minecraft.text.LiteralText
import net.minecraft.util.Util

class DMOSkills : ModInitializer {

    override fun onInitialize() {
        PlayerBlockBreakEvents.AFTER.register { world, player, blockPos, blockState, blockEntity ->
            val playerSkills = DMOSkillsComponents.COMPONENT_PLAYER_SKILLS.get(player).skills
            if (blockState.block in BlockTags.LOGS) {
                playerSkills.increment(1, LUMBERING)
                player.sendSystemMessage(LiteralText("Woodcutting: ${playerSkills[LUMBERING].raw}"), Util.NIL_UUID)
            }

//            HudRenderCallback.EVENT.register { matrices, delta ->
//                MinecraftClient.getInstance().textRenderer.draw(matrices, "hello", 5f, 5f, 0)
//
//            }

            // Text text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light
        }
    }
}
