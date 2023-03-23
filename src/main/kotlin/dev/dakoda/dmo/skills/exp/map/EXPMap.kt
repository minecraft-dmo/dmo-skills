package dev.dakoda.dmo.skills.exp.map

import dev.dakoda.dmo.skills.exp.EXPGain
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack

class EXPMap<T, R : EXPGain.Rules> {
    private val _map = mutableMapOf<KeyMatcher<T>, Entry<R>>()

    fun put(keyMatcher: KeyMatcher<T>, entry: Entry<R>) {
        _map[keyMatcher] = entry
    }

    fun get(t: T): Entry<R>? {
        return _map.entries.firstOrNull { (key, _) -> key.matches(t) }?.value
    }

    fun contains(t: T): Boolean {
        return _map.keys.any { it.matches(t) }
    }

    companion object {
        fun <R : EXPGain.Rules> items() = EXPMap<ItemStack, R>()
        fun <R : EXPGain.Rules> blocks() = EXPMap<BlockState, R>()
    }

    data class Entry<R : EXPGain.Rules>(
        val expGainProvider: EXPGain.Provider,
        val requirements: EXPGain.Rules = EXPGain.Rules(),
        val settings: Settings = Settings()
    ) {
        data class Settings(val order: Order = Order.AFTER) {
            enum class Order { BEFORE, AFTER }
        }
    }
}











