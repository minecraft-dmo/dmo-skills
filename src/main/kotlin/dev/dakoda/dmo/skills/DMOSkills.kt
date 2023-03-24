@file:Suppress("MemberVisibilityCanBePrivate")

package dev.dakoda.dmo.skills

import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMP_SKILLS_DISCOVERED
import dev.dakoda.dmo.skills.component.DMOSkillsComponents.Companion.COMP_SKILLS_EXP
import dev.dakoda.dmo.skills.config.DMOSkillsConfig
import dev.dakoda.dmo.skills.event.PlayerGainEXPCallback
import dev.dakoda.dmo.skills.exp.data.EXPGain
import net.minecraft.client.MinecraftClient
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments.SILK_TOUCH
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import org.apache.logging.log4j.Logger

object DMOSkills {
    const val modID = "dmo-skills"

    lateinit var CONFIG: DMOSkillsConfig
    internal lateinit var LOGGER: Logger

    internal fun resource(location: String) = Identifier(modID, location)

    internal val game: MinecraftClient by lazy { MinecraftClient.getInstance() }

    internal val ItemStack.hasSilkTouch get() = SILK_TOUCH in EnchantmentHelper.get(this).keys
    internal val ItemStack.hasAnyEnchantment get() = EnchantmentHelper.get(this).isNotEmpty()

    fun gainEXP(player: PlayerEntity, gainAmount: Int, gainSkill: Skill.Sub) = gainEXP(player, gainAmount to gainSkill)

    fun gainEXP(player: PlayerEntity, gain: EXPGain) {
        val discoveredSkills = COMP_SKILLS_DISCOVERED[player].skillsDiscovered
        var discoveredNewSkill = false
        if (discoveredSkills[gain.skill] != true) {
            discoveredSkills[gain.skill] = true
            discoveredNewSkill = true
        }

        val playerSkills = COMP_SKILLS_EXP[player].skills
        playerSkills.increase(gain)
        PlayerGainEXPCallback.EVENT.invoker().handle(player, gain, discoveredNewSkill)
        COMP_SKILLS_EXP.sync(player)
        COMP_SKILLS_DISCOVERED.sync(player)
    }

    fun gainEXP(player: PlayerEntity, gain: Pair<Int, Skill.Sub>) = gainEXP(player, EXPGain(gain))
}
