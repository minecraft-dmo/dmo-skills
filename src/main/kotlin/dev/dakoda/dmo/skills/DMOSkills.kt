package dev.dakoda.dmo.skills

import com.google.gson.GsonBuilder
import dev.dakoda.dmo.skills.ModHelper.CONFIG
import dev.dakoda.dmo.skills.ModHelper.LOGGER
import dev.dakoda.dmo.skills.config.DMOSkillsConfig
import dev.dakoda.dmo.skills.exp.AbstractBreakBlockChecker
import dev.dakoda.dmo.skills.exp.AbstractBreakBlockChecker.BreakBlockParams
import dev.dakoda.dmo.skills.exp.BreakBlockChecker
import dev.dakoda.dmo.skills.exp.CombatEXPCheckerOld
import dev.dakoda.dmo.skills.exp.UseBlockEXPCheckerOld
import dev.dakoda.dmo.skills.exp.map.EXPMap
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings.Order
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult.PASS
import org.apache.logging.log4j.LogManager
import java.io.File
import kotlin.io.path.pathString

class DMOSkills : ModInitializer {

    override fun onInitialize() {
        LOGGER = LogManager.getLogger(ModHelper.modID)

        try {
            val configPath = FabricLoader.getInstance().configDir.pathString + "/dmo-skills.json"
            val configFile = File(configPath)

            val gson = GsonBuilder().setPrettyPrinting().create()
            if (!configFile.exists()) {
                val defaultConfig = DMOSkillsConfig.default
                val asJson = gson.toJson(defaultConfig)
                configFile.writeText(asJson)
            }

            val json = configFile.readText()
            CONFIG = gson.fromJson(json, DMOSkillsConfig::class.java)
            // Updates the file with any new config values
            configFile.writeText(gson.toJson(CONFIG))
        } catch (e: Exception) {
            LOGGER.error("Severe failure when reading/writing from mod config file, using default config for now. You should fix that.")
            LOGGER.error(e)
            CONFIG = DMOSkillsConfig.default
        }

        PlayerBlockBreakEvents.AFTER.register { world, player, blockPos, blockState, blockEntity ->
            if (player.isSurvival) {
                BreakBlockChecker.resolve(BreakBlockParams(
                    blockState, blockPos, blockEntity, world, player.mainHandStack
                ), Order.AFTER)?.let {
                    ModHelper.gainEXP(player, it)
                }
            }
        }

        PlayerBlockBreakEvents.BEFORE.register { world, player, blockPos, blockState, blockEntity ->
            if (player.isSurvival) {
                BreakBlockChecker.resolve(BreakBlockParams(
                    blockState, blockPos, blockEntity, world, player.mainHandStack
                ), Order.BEFORE)?.let {
                    ModHelper.gainEXP(player, it)
                }
            }
            true
        }

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register { _, entity, killedEntity ->
            if (entity is PlayerEntity && entity.isSurvival) {
                CombatEXPCheckerOld.Kill.determineEXPGain(entity.mainHandStack, killedEntity).filterNotNull().forEach {
                    ModHelper.gainEXP(entity, it)
                }
            }
        }

        UseBlockCallback.EVENT.register { player, world, _, entityHitResult ->
            if (player.isSurvival) {
                UseBlockEXPCheckerOld.doCallback(world.getBlockState(entityHitResult.blockPos), entityHitResult.blockPos, world, player.mainHandStack)?.let {
                    ModHelper.gainEXP(player, it)
                }
            }
            return@register PASS
        }

        AttackEntityCallback.EVENT.register { player, _, _, hitEntity, _ ->
            if (player.isSurvival) {
                CombatEXPCheckerOld.Attack.determineEXPGain(player.mainHandStack, hitEntity)?.let {
                    ModHelper.gainEXP(player, it)
                }
            }
            return@register PASS
        }
    }

    private val PlayerEntity.isSurvival get() = !this.isCreative && !this.isSpectator
}
