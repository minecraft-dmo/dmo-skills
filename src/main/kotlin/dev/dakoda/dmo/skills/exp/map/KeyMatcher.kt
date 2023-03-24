package dev.dakoda.dmo.skills.exp.map

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey

sealed interface KeyMatcher<T> {
    fun matches(t: T): Boolean

    sealed interface Blocks : KeyMatcher<BlockState> {

        data class Generic(private val block: Block) : Blocks {
            @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            override fun matches(blockState: BlockState): Boolean = blockState.block == block
        }

        data class Tag(private val tagKey: TagKey<Block>) : Blocks {
            @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            override fun matches(blockState: BlockState): Boolean = blockState.isIn(tagKey)
        }
    }

    sealed interface Items : KeyMatcher<ItemStack> {

        data class Generic(private val item: Item) : Items {
            @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            override fun matches(itemStack: ItemStack): Boolean = itemStack.item == item
        }

        data class Tag(private val tagKey: TagKey<Item>) : Items {
            @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            override fun matches(itemStack: ItemStack): Boolean = itemStack.isIn(tagKey)
        }
    }

    sealed interface Entities : KeyMatcher<EntityType<*>> {

        data class Generic(private val _entityType: EntityType<*>) : Entities {
            @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            override fun matches(entityType: EntityType<*>): Boolean = entityType == _entityType
        }

        data class Tag(private val tagKey: TagKey<EntityType<*>>) : Entities {
            @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            override fun matches(entityType: EntityType<*>): Boolean = entityType.isIn(tagKey)
        }
    }
}
