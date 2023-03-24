package dev.dakoda.dmo.skills

import dev.dakoda.dmo.skills.SkillRegistry.register
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items.ANVIL
import net.minecraft.item.Items.BOW
import net.minecraft.item.Items.CAKE
import net.minecraft.item.Items.CHEST_MINECART
import net.minecraft.item.Items.COMPASS
import net.minecraft.item.Items.EMERALD
import net.minecraft.item.Items.ENCHANTED_BOOK
import net.minecraft.item.Items.IRON_SWORD
import net.minecraft.item.Items.MILK_BUCKET
import net.minecraft.item.Items.NETHER_STAR
import net.minecraft.item.Items.OAK_SAPLING
import net.minecraft.item.Items.POTION
import net.minecraft.item.Items.RAW_COPPER
import net.minecraft.item.Items.STONE
import net.minecraft.item.Items.SWEET_BERRIES
import net.minecraft.item.Items.TROPICAL_FISH
import net.minecraft.item.Items.WHEAT
import net.minecraft.text.Text

object SkillRegistry {

    val registered: MutableList<Skill> = mutableListOf()

    fun SubSkill.register(): SubSkill {
        registered.add(this)
        return this
    }

    fun SkillCategory.register(): SkillCategory {
        this.subSkills.forEach {
            it.parent = this
        }
        registered.add(this)
        return this
    }
}

sealed class Skill(
    val name: String,
    val icon: Item
) {
    val translatableText get() = Text.translatable("dmo.skills.${name.lowercase()}")

    fun stack(): ItemStack = icon.defaultStack

    companion object {
        val allCategories: List<SkillCategory> get() = SkillRegistry.registered.filterIsInstance<SkillCategory>()
        val allSubSkills: List<SubSkill> get() = SkillRegistry.registered.filterIsInstance<SubSkill>()
        val all get() = allCategories + allSubSkills

        val LUMBERING = SubSkill(name = "LUMBERING", icon = OAK_SAPLING).register()
        val MINING = SubSkill(name = "MINING", icon = RAW_COPPER).register()
        val FORAGING = SubSkill(name = "FORAGING", icon = SWEET_BERRIES).register()
        val FISHING = SubSkill(name = "FISHING", icon = TROPICAL_FISH).register()
        val CULTIVATION = SubSkill(name = "CULTIVATION", icon = WHEAT).register()
        val ANIMAL_CARE = SubSkill(name = "ANIMAL_CARE", icon = MILK_BUCKET).register()
        val TRADING = SubSkill(name = "TRADING", icon = EMERALD).register()
        val CARTOGRAPHY = SubSkill(name = "CARTOGRAPHY", icon = COMPASS).register()
        val DUNGEONEER = SubSkill(name = "DUNGEONEER", icon = CHEST_MINECART).register()
        val MELEE = SubSkill(name = "MELEE", icon = IRON_SWORD).register()
        val RANGER = SubSkill(name = "RANGER", icon = BOW).register()
        val HUNTER = SubSkill(name = "HUNTER", icon = NETHER_STAR).register()
        val ALCHEMY = SubSkill(name = "ALCHEMY", icon = POTION).register()
        val ENCHANTING = SubSkill(name = "ENCHANTING", icon = ENCHANTED_BOOK).register()
        val METALWORK = SubSkill(name = "METALWORK", icon = ANVIL).register()
        val COOKING = SubSkill(name = "COOKING", icon = CAKE).register()

        val NULL_SKILL = SubSkill(name = "NULL", icon = STONE)
        val VARIANT = SubSkill(name = "VARIANT", icon = STONE)
        val NULL_CAT = SkillCategory(name = "NULL", icon = STONE)

        val GATHERING = SkillCategory(
            name = "GATHERING",
            icon = OAK_SAPLING,
            subSkills = arrayOf(
                LUMBERING,
                MINING,
                FORAGING,
                FISHING
            )
        ).register()
        val FARMING = SkillCategory(
            name = "FARMING",
            icon = WHEAT,
            subSkills = arrayOf(
                CULTIVATION,
                ANIMAL_CARE
            )
        ).register()
        val MERCHANT = SkillCategory(
            name = "MERCHANT",
            icon = EMERALD,
            subSkills = arrayOf(
                TRADING
            )
        ).register()
        val EXPLORER = SkillCategory(
            name = "EXPLORER",
            icon = COMPASS,
            subSkills = arrayOf(
                CARTOGRAPHY,
                DUNGEONEER
            )
        ).register()
        val COMBAT = SkillCategory(
            name = "COMBAT",
            icon = IRON_SWORD,
            subSkills = arrayOf(
                MELEE,
                RANGER,
                HUNTER
            )
        ).register()
        val CRAFTING = SkillCategory(
            name = "CRAFTING",
            icon = POTION,
            subSkills = arrayOf(
                ALCHEMY,
                ENCHANTING,
                METALWORK,
                COOKING
            )
        ).register()
    }
}

class SkillCategory(name: String, icon: Item, vararg val subSkills: SubSkill) : Skill(name, icon)

open class SubSkill(name: String, icon: Item) : Skill(name, icon) {

    lateinit var parent: SkillCategory
}
