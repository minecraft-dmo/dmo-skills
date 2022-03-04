package dev.dakoda.dmo.skills

import dev.dakoda.dmo.skills.Skill.Sub.LUMBERING
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMP_SKILLS_EXP
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMP_SKILLS_TRACKED
import dev.dakoda.dmo.skills.component.SkillsTrackedComponent
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.tag.BlockTags
import net.minecraft.util.Identifier

class DMOSkills : ModInitializer {

    override fun onInitialize() {
        PlayerBlockBreakEvents.AFTER.register { world, player: PlayerEntity, blockPos, blockState, blockEntity ->
            val playerSkills = COMP_SKILLS_EXP.get(player).skills
            if (blockState.block in BlockTags.LOGS) {
                playerSkills.increment(1, LUMBERING)
                COMP_SKILLS_EXP.sync(player)
            }
        }
    }
}
