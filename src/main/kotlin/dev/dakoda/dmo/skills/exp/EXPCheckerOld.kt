package dev.dakoda.dmo.skills.exp

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey

interface EXPCheckerOld {

    fun BlockState.of(tag: TagKey<Block>) = isIn(tag)
    fun BlockState.eq(block: Block) = this.block == block
    fun BlockState.eq(vararg blocks: Block) = block in blocks

    fun ItemStack.of(tag: TagKey<Item>) = isIn(tag)
    fun ItemStack.eq(item: Item) = this.item == item
    fun ItemStack.eq(vararg items: Item) = item in items
}
