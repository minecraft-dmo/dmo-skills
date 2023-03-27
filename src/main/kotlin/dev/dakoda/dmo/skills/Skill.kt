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

sealed class Skill(
    val name: String,
    val icon: Item
) {
    fun stack(): ItemStack = icon.defaultStack

    companion object {
        val allCategories: List<Category> get() = SkillRegistry.registered.filterIsInstance<Category>()
        val allSubSkills: List<Sub> get() = SkillRegistry.registered.filterIsInstance<Sub>()
        val all get() = allCategories + allSubSkills

        val LUMBERING = Sub(name = "LUMBERING", icon = OAK_SAPLING).register()
        val MINING = Sub(name = "MINING", icon = RAW_COPPER).register()
        val FORAGING = Sub(name = "FORAGING", icon = SWEET_BERRIES).register()
        val FISHING = Sub(name = "FISHING", icon = TROPICAL_FISH).register()
        val CULTIVATION = Sub(name = "CULTIVATION", icon = WHEAT).register()
        val ANIMAL_CARE = Sub(name = "ANIMAL_CARE", icon = MILK_BUCKET).register()
        val TRADING = Sub(name = "TRADING", icon = EMERALD).register()
        val DUNGEONEER = Sub(name = "DUNGEONEER", icon = CHEST_MINECART).register()
        val MELEE = Sub(name = "MELEE", icon = IRON_SWORD).register()
        val RANGER = Sub(name = "RANGER", icon = BOW).register()
        val HUNTER = Sub(name = "HUNTER", icon = NETHER_STAR).register()
        val ALCHEMY = Sub(name = "ALCHEMY", icon = POTION).register()
        val ENCHANTING = Sub(name = "ENCHANTING", icon = ENCHANTED_BOOK).register()
        val METALWORK = Sub(name = "METALWORK", icon = ANVIL).register()
        val COOKING = Sub(name = "COOKING", icon = CAKE).register()

        val NULL_SKILL = Sub(name = "NULL", icon = STONE)
        val VARIANT = Sub(name = "VARIANT", icon = STONE)
        val NULL_CAT = Category(name = "NULL", icon = STONE)

        val GATHERING = Category(
            name = "GATHERING",
            icon = OAK_SAPLING,
            subSkills = arrayOf(
                LUMBERING,
                MINING,
                FORAGING,
                FISHING
            )
        ).register()
        val FARMING = Category(
            name = "FARMING",
            icon = WHEAT,
            subSkills = arrayOf(
                CULTIVATION,
                ANIMAL_CARE
            )
        ).register()
        val MERCHANT = Category(
            name = "MERCHANT",
            icon = EMERALD,
            subSkills = arrayOf(
                TRADING
            )
        ).register()
        val EXPLORER = Category(
            name = "EXPLORER",
            icon = COMPASS,
            subSkills = arrayOf(
                DUNGEONEER
            )
        ).register()
        val COMBAT = Category(
            name = "COMBAT",
            icon = IRON_SWORD,
            subSkills = arrayOf(
                MELEE,
                RANGER,
                HUNTER
            )
        ).register()
        val CRAFTING = Category(
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

    class Category(name: String, icon: Item, vararg val subSkills: Sub) : Skill(name, icon)

    open class Sub(name: String, icon: Item) : Skill(name, icon) {

        lateinit var parent: Category
    }
}
