package dev.dakoda.dmo.skills

import net.minecraft.item.ItemStack
import net.minecraft.text.Text

sealed interface TrackableSkill {

    val translatableText: Text

    val name: String

    fun stack(): ItemStack
}
