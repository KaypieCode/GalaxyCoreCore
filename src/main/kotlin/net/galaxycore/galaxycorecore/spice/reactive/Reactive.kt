package net.galaxycore.galaxycorecore.spice.reactive

class Reactive<T> (val value: T) {
    private var item : T = value
    private val listeners = mutableListOf<(T) -> Unit>()

    fun getItem() : T {
        return item
    }

    fun setItem(newItem: T): T {
        item = newItem
        listeners.forEach { it(item) }
        return item
    }

    fun updatelistener(listener: (T) -> Unit) {
        listeners.add(listener)
    }

    fun update(callback: (T) -> T) {
        setItem(callback(item))
    }
}