package scheduleTrigger

import java.util.*

class ScheduleField(field: String, min: Int, max: Int) {
    var value: BitSet = BitSet(max)
    var isPeriodic = false

    init {
        initField(field, min, max)
    }

    private fun initField(value: String, min: Int, max: Int) {
        var minValue = 0
        var maxValue = 0
        if (value == "*") {
            minValue = min
            maxValue = max - 1
            isPeriodic = true
        } else {
            minValue = value.toInt()
            maxValue = value.toInt()
        }

        if (minValue >= max || maxValue >= max) {
            throw IllegalArgumentException("Value exceeds maximum")
        }
        if (minValue < min || maxValue < min) {
            throw IllegalArgumentException("Value less than minimum")
        }
        if (minValue > maxValue) {
            throw IllegalArgumentException("Invalid min and max value")
        }

        this.value.set(minValue, maxValue + 1)
    }

    fun next(index: Int): Int {
        return this.value.nextSetBit(index)
    }

    fun getFirst(): Int {
        return this.value.nextSetBit(0)
    }

    fun get(index: Int): Boolean {
        return this.value.get(index)
    }
}