package dev.dakoda.dmo.skills.gui

import net.minecraft.client.util.Window

val Window.leftOfInventory get() = (this.scaledWidth / 2) - 110
val Window.rightOfInventory get() = (this.scaledWidth / 2) + 90
val Window.topOfInventory get() = (this.scaledHeight / 2) - 80
