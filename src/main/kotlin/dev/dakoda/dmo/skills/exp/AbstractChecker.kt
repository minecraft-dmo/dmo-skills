package dev.dakoda.dmo.skills.exp

import dev.dakoda.dmo.skills.SubSkill
import dev.dakoda.dmo.skills.exp.data.EXPGain
import dev.dakoda.dmo.skills.exp.map.EXPMap
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings.Order
import dev.dakoda.dmo.skills.exp.map.KeyMatcher
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey

abstract class AbstractChecker<Key, Params : EXPGain.Provider.Params, R : EXPGain.Rules> {
    abstract val registry: EXPMap<*, R>

    abstract fun haveEntryFor(key: Key, order: Order): Boolean
    abstract fun getEntry(key: Key, order: Order): EXPMap.Entry<R>?
    abstract fun resolve(params: Params, order: Order): EXPGain?

    abstract class ChecksItems<Params : EXPGain.Provider.Params, R : EXPGain.Rules> : AbstractChecker<Item, Params, R>() {
        protected operator fun <R : EXPGain.Rules> EXPMap<ItemStack, R>.set(item: Item, entry: EXPMap.Entry<R>) = put(KeyMatcher.Items.Generic(item), entry)
        protected operator fun <R : EXPGain.Rules> EXPMap<ItemStack, R>.set(tag: TagKey<Item>, entry: EXPMap.Entry<R>) = put(KeyMatcher.Items.Tag(tag), entry)
    }

    abstract class ChecksBlocks<Params : EXPGain.Provider.Params, R : EXPGain.Rules> : AbstractChecker<Block, Params, R>() {
        protected operator fun <R : EXPGain.Rules> EXPMap<BlockState, R>.set(block: Block, entry: EXPMap.Entry<R>) = put(KeyMatcher.Blocks.Generic(block), entry)
        protected operator fun <R : EXPGain.Rules> EXPMap<BlockState, R>.set(tag: TagKey<Block>, entry: EXPMap.Entry<R>) = put(KeyMatcher.Blocks.Tag(tag), entry)
    }

    abstract class ChecksEntities<Params : EXPGain.Provider.Params, R : EXPGain.Rules> : AbstractChecker<EntityType<*>, Params, R>() {
        protected operator fun <R : EXPGain.Rules> EXPMap<EntityType<*>, R>.set(entity: EntityType<*>, entry: EXPMap.Entry<R>) = put(KeyMatcher.Entities.Generic(entity), entry)
        protected operator fun <R : EXPGain.Rules> EXPMap<EntityType<*>, R>.set(tag: TagKey<EntityType<*>>, entry: EXPMap.Entry<R>) = put(KeyMatcher.Entities.Tag(tag), entry)
    }

    val Pair<Int, SubSkill>.expGain get() = EXPGain(this)
}
