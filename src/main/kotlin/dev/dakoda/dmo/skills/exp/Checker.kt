package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.exp.map.EXPMap
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings
import dev.dakoda.dmo.skills.exp.map.KeyMatcher
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey

abstract class Checker<Key, Params : EXPGain.Provider.Params, R : EXPGain.Rules> {
    abstract val registry: EXPMap<*, R>

    abstract fun haveEntryFor(key: Key, order: Settings.Order): Boolean
    abstract fun getEntry(key: Key, order: Settings.Order): EXPMap.Entry<R>?
    abstract fun resolve(params: Params, order: Settings.Order): EXPGain?

    operator fun <R : EXPGain.Rules> EXPMap<ItemStack, R>.set(item: Item, entry: EXPMap.Entry<R>) = put(KeyMatcher.Items.Generic(item), entry)
    operator fun <R : EXPGain.Rules> EXPMap<ItemStack, R>.set(tag: TagKey<Item>, entry: EXPMap.Entry<R>) = put(KeyMatcher.Items.Tag(tag), entry)
    operator fun <R : EXPGain.Rules> EXPMap<BlockState, R>.set(block: Block, entry: EXPMap.Entry<R>) = put(KeyMatcher.Blocks.Generic(block), entry)
    operator fun <R : EXPGain.Rules> EXPMap<BlockState, R>.set(tag: TagKey<Block>, entry: EXPMap.Entry<R>) = put(KeyMatcher.Blocks.Tag(tag), entry)

    protected fun settings(order: Settings.Order) = Settings(order)
}