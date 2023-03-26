package dev.dakoda.dmo.skills.exp.map

import dev.dakoda.dmo.skills.exp.data.EXPGain
import dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings.Order.DONT_CARE
import net.minecraft.block.BlockState
import net.minecraft.entity.EntityType
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion

class EXPMap<T, R : EXPGain.Rules> {
    private val _map = mutableMapOf<KeyMatcher<T>, Entry<R>>()

    fun put(keyMatcher: KeyMatcher<T>, entry: Entry<R>) {
        _map[keyMatcher] = entry
    }

    fun get(t: T, order: Entry.Settings.Order): Entry<R>? {
        return _map.entries.firstOrNull { (key, entry) ->
            entry.settings.order == order && key.matches(t)
        }?.value
    }

    fun contains(t: T, order: Entry.Settings.Order): Boolean {
        return _map.any { (matcher, entry) ->
            entry.settings.order == order && matcher.matches(t)
        }
    }

    companion object {
        fun <R : EXPGain.Rules> items() = EXPMap<ItemStack, R>()
        fun <R : EXPGain.Rules> blocks() = EXPMap<BlockState, R>()
        fun <R : EXPGain.Rules> entities() = EXPMap<EntityType<*>, R>()
        fun <R : EXPGain.Rules> potions() = EXPMap<Potion, R>()
    }

    data class Entry<R : EXPGain.Rules>(
        val expGainProvider: EXPGain.Provider,
        val rules: R,
        val settings: Settings = Settings(DONT_CARE)
    ) {
        open class Settings(val order: Order) {
            enum class Order { BEFORE, AFTER, DONT_CARE }
        }
    }
}
