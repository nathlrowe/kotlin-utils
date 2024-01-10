package io.github.nathlrowe.utils

// TODO extend this to essentially replace the kotlin "Progression" concept, and make it not package-private

class CharProgressionIterator(first: Char, last: Char, val step: Int) : CharIterator() {
    private val lastCode: Int = last.code
    private var hasNext: Boolean = if (step > 0) first <= last else first >= last
    private var next: Int = if (hasNext) first.code else lastCode

    override fun hasNext(): Boolean = hasNext

    override fun nextChar(): Char {
        val value = next
        if (value == lastCode) {
            if (!hasNext) throw NoSuchElementException()
            hasNext = false
        } else {
            next += step
        }
        return value.toChar()
    }
}

class IntProgressionIterator(first: Int, val last: Int, val step: Int) : IntIterator() {
    private var hasNext: Boolean = if (step > 0) first <= last else first >= last
    private var next: Int = if (hasNext) first else last

    override fun hasNext(): Boolean = hasNext

    override fun nextInt(): Int {
        val value = next
        if (value == last) {
            if (!hasNext) throw NoSuchElementException()
            hasNext = false
        } else {
            next += step
        }
        return value
    }
}

class LongProgressionIterator(first: Long, val last: Long, val step: Long) : LongIterator() {
    private var hasNext: Boolean = if (step > 0) first <= last else first >= last
    private var next: Long = if (hasNext) first else last

    override fun hasNext(): Boolean = hasNext

    override fun nextLong(): Long {
        val value = next
        if (value == last) {
            if (!hasNext) throw NoSuchElementException()
            hasNext = false
        } else {
            next += step
        }
        return value
    }
}

// TODO Float/Double progression iterators
// TODO UInt/ULong progression iterators
// TODO a generic progression iterator on <T : Comparable<T>> with a step lambda
