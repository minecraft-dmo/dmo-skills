package dev.dakoda.dmo.skills

import net.minecraft.item.ItemStack
import net.minecraft.text.TranslatableText

sealed interface TrackableSkill {

    val translatableText: TranslatableText

    val name: String

    fun stack(): ItemStack
}
