package scheduleTrigger

import java.util.*

enum class PositionType(val value: Int) {
    SECOND(0),
    MINUTE(1),
    HOUR(2),
    DAY_OF_MONTH(3),
    MONTH(4),
    DAY_OF_WEEK(5)
}

class ScheduleExpression(schedule: String) {
    private var expr: String = schedule

    private var seconds: BitSet = BitSet(60)
    private var minutes: BitSet = BitSet(60)
    private var hours: BitSet = BitSet(24)
    private var daysOfMonth: BitSet = BitSet(31)
    private var months: BitSet = BitSet(12)
    private var daysOfWeek: BitSet = BitSet(7)

    init {
        buildExpression(schedule)
    }

    private fun buildExpression(schedule: String) {
        val separate = schedule.split(" ", "\t").map { it.trim() }

        if (!areValidCronFields(separate)) {
            throw IllegalArgumentException("Expression is not valid")
        }

        separate.forEachIndexed { position, term ->
            when (position) {
                PositionType.SECOND.value -> setNumberHits(this.seconds, term, max=60)
                PositionType.MINUTE.value -> setNumberHits(this.minutes, term,  max=60)
                PositionType.HOUR.value -> setNumberHits(this.hours, term, max=60)
                PositionType.DAY_OF_MONTH.value -> setDaysOfMonth(term)
                PositionType.MONTH.value -> setMonths(term)
                PositionType.DAY_OF_WEEK.value -> setDaysOfWeek(term)
            }
        }
    }

    private fun areValidCronFields(separate: List<String>) : Boolean {
        return (separate.isNotEmpty() && separate.size == 6)
    }

    private fun setNumberHits(bits: BitSet, value: String, min: Int = 0, max: Int) {
        val (rangeMin, rangeMax) = getRange(value, min, max)
        bits.set(rangeMin, rangeMax + 1)
    }

    private fun getRange(value: String, min: Int, max: Int): Pair<Int, Int> {
        var rangeMin: Int = 0
        var rangeMax: Int = 0

        if (value == "*") {
            rangeMin = min
            rangeMax = max - 1
        } else {
            rangeMin = value.toInt()
            rangeMax = value.toInt()
        }

        if (rangeMin >= max || rangeMax >= max) {
            throw IllegalArgumentException("Range exceeds maximum (" + max + "): '" +
                    value + "' in expression \"" + this.expr + "\"")
        }
        if (rangeMin < min || rangeMax < min) {
            throw IllegalArgumentException("Range less than minimum (" + min + "): '" +
                    value + "' in expression \"" + this.expr + "\"")
        }
        if (rangeMin > rangeMax) {
            throw IllegalArgumentException("Invalid inverted range: '" + value +
                    "' in expression \"" + this.expr + "\"")
        }
        return Pair(rangeMin, rangeMax)
    }

    private fun setDaysOfMonth(value: String) {
        val max = 31
        setNumberHits(this.daysOfMonth, value, max=max + 1)
        this.daysOfMonth.clear(0)
    }

    private fun setMonths(value: String) {
        val max = 12
        val months = BitSet(13)
        setNumberHits(months, value, 1, max + 1)
        for (i in 1..max) {
            if (months.get(i)) {
                this.months.set(i - 1)
            }
        }
    }

    private fun setDaysOfWeek(value: String) {
        setNumberHits(this.daysOfWeek, value, max=8)
        if (this.daysOfWeek.get(7)) {
            this.daysOfWeek.set(0)
            this.daysOfWeek.clear(7)
        }
    }
}
