package dev.dakoda.dmo.skills

import dev.dakoda.dmo.skills.Skill.Sub.ALCHEMY
import dev.dakoda.dmo.skills.Skill.Sub.ANIMAL_CARE
import dev.dakoda.dmo.skills.Skill.Sub.ARCHERY
import dev.dakoda.dmo.skills.Skill.Sub.CARTOGRAPHY
import dev.dakoda.dmo.skills.Skill.Sub.COOKING
import dev.dakoda.dmo.skills.Skill.Sub.CULTIVATION
import dev.dakoda.dmo.skills.Skill.Sub.DUNGEONEER
import dev.dakoda.dmo.skills.Skill.Sub.ENCHANTING
import dev.dakoda.dmo.skills.Skill.Sub.FISHING
import dev.dakoda.dmo.skills.Skill.Sub.FORAGING
import dev.dakoda.dmo.skills.Skill.Sub.HUNTER
import dev.dakoda.dmo.skills.Skill.Sub.LUMBERING
import dev.dakoda.dmo.skills.Skill.Sub.MELEE
import dev.dakoda.dmo.skills.Skill.Sub.METALWORK
import dev.dakoda.dmo.skills.Skill.Sub.MINING
import dev.dakoda.dmo.skills.Skill.Sub.TRADING
import net.minecraft.item.Item
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
import net.minecraft.text.TranslatableText

enum class Skill(val icon: Item, vararg val subSkills: Sub) : TrackableSkill {

    NULL(icon = STONE),
    GATHERING(icon = OAK_SAPLING, subSkills = arrayOf(LUMBERING, MINING, FORAGING, FISHING)),
    FARMING(icon = WHEAT, subSkills = arrayOf(CULTIVATION, ANIMAL_CARE)),
    MERCHANT(icon = EMERALD, subSkills = arrayOf(TRADING)),
    EXPLORER(icon = COMPASS, subSkills = arrayOf(CARTOGRAPHY, DUNGEONEER)),
    COMBAT(icon = IRON_SWORD, subSkills = arrayOf(MELEE, ARCHERY, HUNTER)),
    CRAFTING(icon = POTION, subSkills = arrayOf(ALCHEMY, ENCHANTING, METALWORK, COOKING));

    init {
        this.subSkills.forEach {
            it.parent = this
        }
    }

    override fun stack() = icon.defaultStack

    override val translatableText get() = TranslatableText("dmo.skills.${name.lowercase()}")

    companion object {
        val all: List<TrackableSkill> get() = (values().toList() + Sub.values())
    }

    enum class Sub(val icon: Item) : TrackableSkill {

        LUMBERING(icon = OAK_SAPLING), MINING(icon = RAW_COPPER), FORAGING(icon = SWEET_BERRIES), FISHING(icon = TROPICAL_FISH),
        CULTIVATION(icon = WHEAT), ANIMAL_CARE(icon = MILK_BUCKET),
        TRADING(icon = EMERALD),
        CARTOGRAPHY(icon = COMPASS), DUNGEONEER(icon = CHEST_MINECART),
        MELEE(icon = IRON_SWORD), ARCHERY(icon = BOW), HUNTER(icon = NETHER_STAR),
        ALCHEMY(icon = POTION), ENCHANTING(icon = ENCHANTED_BOOK), METALWORK(icon = ANVIL), COOKING(icon = CAKE);

        lateinit var parent: Skill

        override fun stack() = icon.defaultStack

        override val translatableText get() = TranslatableText("dmo.skills.${parent.name.lowercase()}.${name.lowercase()}")
    }
}
