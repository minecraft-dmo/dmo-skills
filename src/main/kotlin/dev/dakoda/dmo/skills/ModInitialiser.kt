package dev.dakoda.dmo.skills

import com.google.gson.GsonBuilder
import dev.dakoda.dmo.skills.DMOSkills.CONFIG
import dev.dakoda.dmo.skills.DMOSkills.LOGGER
import dev.dakoda.dmo.skills.config.DMOSkillsConfig
import dev.dakoda.dmo.skills.exp.AbstractBreakBlockChecker.BreakBlockParams
import dev.dakoda.dmo.skills.exp.AbstractEntityKillChecker.EntityKillParams
import dev.dakoda.dmo.skills.exp.AbstractUseBlockChecker.UseBlockParams
import dev.dakoda.dmo.skills.exp.BreakBlockChecker
import dev.dakoda.dmo.skills.exp.CookingChecker
import dev.dakoda.dmo.skills.exp.EntityHuntChecker
import dev.dakoda.dmo.skills.exp.EntityKillChecker
import dev.dakoda.dmo.skills.exp.FishingChecker
import dev.dakoda.dmo.skills.exp.UseBlockChecker
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings.Order.AFTER
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings.Order.BEFORE
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings.Order.DONT_CARE
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult.PASS
import org.apache.logging.log4j.LogManager
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.pathString

class ModInitialiser : ModInitializer {

    override fun onInitialize() {
        LOGGER = LogManager.getLogger(DMOSkills.modID)

        try {
            val root = FabricLoader.getInstance().configDir.pathString

            Files.createDirectories(Paths.get("$root/dmo"))
            Files.createDirectories(Paths.get("$root/dmo/skills"))

            val configPath = "$root/dmo/skills/config.json"
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

        initialiseCheckers()

        PlayerBlockBreakEvents.AFTER.register { world, player, blockPos, blockState, blockEntity ->
            if (player.isSurvival) {
                BreakBlockChecker.resolve(
                    BreakBlockParams(
                        world,
                        player.mainHandStack,
                        blockPos,
                        blockState,
                        blockEntity
                    ),
                    order = AFTER
                )?.let {
                    DMOSkills.gainEXP(player, it)
                }
            }
        }

        PlayerBlockBreakEvents.BEFORE.register { world, player, blockPos, blockState, blockEntity ->
            if (player.isSurvival) {
                BreakBlockChecker.resolve(
                    BreakBlockParams(
                        world,
                        player.mainHandStack,
                        blockPos,
                        blockState,
                        blockEntity
                    ),
                    order = BEFORE
                )?.let {
                    DMOSkills.gainEXP(player, it)
                }
            }
            true
        }

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register { world, entity, killedEntity ->
            if (entity is PlayerEntity && entity.isSurvival) {
                EntityKillChecker.resolve(EntityKillParams(world, entity, killedEntity), order = DONT_CARE)?.let {
                    DMOSkills.gainEXP(entity, it)
                }
                EntityHuntChecker.resolve(EntityKillParams(world, entity, killedEntity), order = DONT_CARE)?.let {
                    DMOSkills.gainEXP(entity, it)
                }
            }
        }

        UseBlockCallback.EVENT.register { player, world, _, entityHitResult ->
            if (player.isSurvival) {
                UseBlockChecker.resolve(
                    UseBlockParams(
                        player.mainHandStack,
                        world,
                        world.getBlockState(entityHitResult.blockPos),
                        entityHitResult.blockPos
                    ),
                    order = DONT_CARE
                )?.let {
                    DMOSkills.gainEXP(player, it)
                }
            }
            return@register PASS
        }
    }

    private val PlayerEntity.isSurvival get() = !this.isCreative && !this.isSpectator

    private fun initialiseCheckers() {
        BreakBlockChecker; EntityHuntChecker; EntityKillChecker; FishingChecker

        if (FabricLoader.getInstance().isModLoaded("dmo-cooking")) {
            CONFIG.exp.cooking.overridden = true
            println("Cooking EXP has been overridden by dmo-cooking; disabling crafting hooks")
        } else {
            CookingChecker
        }
    }
}
