package dev.dakoda.dmo.skills

import net.minecraft.text.TranslatableText

sealed interface TrackableSkill {

    val translatableText: TranslatableText

    val name: String
}