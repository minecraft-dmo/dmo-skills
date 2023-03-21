@file:Suppress("MemberVisibilityCanBePrivate")

package dev.dakoda.dmo.skills

import dev.dakoda.dmo.skills.Skills.EXP
import dev.dakoda.dmo.skills.Skills.EXP.Companion.NULL
import net.fabricmc.fabric.api.client.screen.v1.Screens
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.util.Window
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments.SILK_TOUCH
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.Util.NIL_UUID
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object ModHelper {
    const val modID = "dmo-skills"

    fun resource(location: String) = Identifier(modID, location)

    val game: MinecraftClient by lazy { MinecraftClient.getInstance() }

    val Screen.buttons: MutableList<ClickableWidget>
        get() = Screens.getButtons(this)

    fun ClientPlayerEntity?.debugMessage(text: String) = this?.sendSystemMessage(Text.of(text), NIL_UUID)

    fun List<EXP>.find(trackableSkill: TrackableSkill) = this.find { it.skill == trackableSkill } ?: NULL

    val Window.leftOfInventory get() = (this.scaledWidth / 2) - 110
    val Window.rightOfInventory get() = (this.scaledWidth / 2) + 90
    val Window.topOfInventory get() = (this.scaledHeight / 2) - 80

    val ItemStack.hasSilkTouch get() = SILK_TOUCH in EnchantmentHelper.get(this).keys
    val ItemStack.hasAnyEnchantment get() = EnchantmentHelper.get(this).isNotEmpty()

    fun BlockPos.horizontals(world: World) = listOf(north(), east(), south(), west()).map {
        world.getBlockState(it)
    }
}
