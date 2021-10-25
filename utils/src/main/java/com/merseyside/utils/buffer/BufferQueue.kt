package com.merseyside.utils.buffer

class BufferQueue<T>(private val capacity: Int) {

    val queue: ArrayDeque<T> = ArrayDeque(capacity)
    val size: Int
    get() {
        return queue.size
    }

    fun add(value: T) {
        with(queue) {
            if (size == capacity) removeFirstOrNull()
            queue.addLast(value)
        }
    }

    fun get(index: Int): T? {
        return try {
            queue[index]
        } catch (e: IndexOutOfBoundsException) {
            if (index >= capacity) throw IllegalArgumentException("Capacity is $capacity")
            else null
        }
    }

    fun getLast(): T? {
        return queue.lastOrNull()
    }

    fun getFirst(): T? {
        return queue.firstOrNull()
    }

    fun clear() {
        queue.clear()
    }

    fun remove(index: Int): T {
        return queue.removeAt(index)
    }

    fun remove(element: T): Boolean {
        return queue.remove(element)
    }

    fun isEmpty(): Boolean = queue.isEmpty()

    fun isNotEmpty(): Boolean = queue.isNotEmpty()

    operator fun <T> BufferQueue<T>.get(index: Int): T? {
        return get(index)
    }

}

