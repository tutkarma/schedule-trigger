package scheduleTrigger

import java.util.*
import kotlin.collections.ArrayList


enum class PositionType(val value: Int) {
    SECOND(0),
    MINUTE(1),
    HOUR(2),
    DAY_OF_MONTH(3),
    MONTH(4),
    DAY_OF_WEEK(5)
}

class ScheduleExpression(schedule: String) {

    private lateinit var seconds: ScheduleField
    private lateinit var minutes: ScheduleField
    private lateinit var hours: ScheduleField
    private lateinit var daysOfMonth: ScheduleField
    private lateinit var months: ScheduleField
    private lateinit var daysOfWeek: ScheduleField

    private val MAX_COUNT_DAYS_IN_YEAR = 366

    val isPeriodic: Boolean
        get() = (this.seconds.isPeriodic ||
                this.minutes.isPeriodic ||
                this.hours.isPeriodic ||
                this.daysOfMonth.isPeriodic ||
                this.months.isPeriodic ||
                this.daysOfWeek.isPeriodic)

    init {
        buildExpression(schedule)
    }

    private fun buildExpression(schedule: String) {
        val separateFields = schedule.split(" ", "\t").map { it.trim() }

        if (!isValidExpression(separateFields)) {
            throw IllegalArgumentException("Expression is not valid")
        }

        separateFields.forEachIndexed { position, term ->
            when (position) {
                PositionType.SECOND.value -> this.seconds = ScheduleField(term, 0, 60)
                PositionType.MINUTE.value -> this.minutes = ScheduleField(term, 0, 60)
                PositionType.HOUR.value -> this.hours = ScheduleField(term, 0, 24)
                PositionType.DAY_OF_MONTH.value -> this.daysOfMonth = ScheduleField(term, 1, 32)
                PositionType.MONTH.value -> {
                    val validTerm = if (term == "*") term else (term.toInt() - 1).toString()
                    this.months = ScheduleField(validTerm, 0, 12)
                }
                PositionType.DAY_OF_WEEK.value -> this.daysOfWeek = ScheduleField(term, 0, 7)
            }
        }
    }

    private fun isValidExpression(separateFields: List<String>): Boolean {
        if (separateFields.isNullOrEmpty()) {
            return false
        }

        if (separateFields.size != 6) {
            return false
        }

        for (field in separateFields) {
            if (field == "*")
                continue
            for (c in field) {
                if (!c.isDigit())
                    return false
            }
        }

        return true
    }

    fun next(date: Date): Date {
        val calendar = GregorianCalendar()
        calendar.time = date
        calendar.set(Calendar.MILLISECOND, 0)
        getNext(calendar)

        if (calendar.time == date) {
            calendar.add(Calendar.SECOND, 1)
            getNext(calendar)
        }

        return calendar.time
    }

    private fun getNext(calendar: Calendar) {
        while (true) {
            val usedFields = ArrayList<Int>()

            if (!findNext(calendar, this.seconds, Calendar.SECOND, Calendar.MINUTE, usedFields)) {
                continue
            }

            if (!findNext(calendar, this.minutes, Calendar.MINUTE, Calendar.HOUR_OF_DAY, usedFields)) {
                continue
            }

            if (!findNext(calendar, this.hours, Calendar.HOUR_OF_DAY, Calendar.DAY_OF_WEEK, usedFields)) {
                continue
            }

            if (!findNextDay(calendar, this.daysOfMonth, this.daysOfWeek, usedFields)) {
                continue
            }

            if (findNext(calendar, this.months, Calendar.MONTH, Calendar.YEAR, usedFields)) {
                break
            }
        }
    }

    private fun findNext(calendar: Calendar,
                         field: ScheduleField,
                         currentField: Int,
                         nextField: Int,
                         usedFields: ArrayList<Int>): Boolean {
        val currentValue = calendar.get(currentField)
        var nextValue = field.next(currentValue)

        if (nextValue == -1) {
            calendar.add(nextField, 1)
            calendar.set(currentField, if (currentField == Calendar.DAY_OF_MONTH) 1 else 0)
            nextValue = field.getFirst()
        }

        if (nextValue != currentValue) {
            calendar.set(currentField, nextValue)
            usedFields.forEach {
                calendar.set(it, if (it == Calendar.DAY_OF_MONTH) 1 else 0)
            }
        }

        usedFields.add(currentField)
        return (nextValue == currentValue && currentField != Calendar.MONTH) ||
                (nextValue == currentValue && currentField == Calendar.MONTH)
    }

    private fun findNextDay(calendar: Calendar,
                            daysOfMonth: ScheduleField,
                            daysOfWeek: ScheduleField,
                            usedFields: ArrayList<Int>): Boolean {
        var cnt = 0
        var currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        var nextDayOfMonth = currentDayOfMonth

        while ((!daysOfMonth.get(nextDayOfMonth) || !daysOfWeek.get(currentDayOfWeek - 1)) &&
                cnt < MAX_COUNT_DAYS_IN_YEAR) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            nextDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            usedFields.forEach {
                calendar.set(it, if (it == Calendar.DAY_OF_MONTH) 1 else 0)
            }
            cnt++
        }

        if (cnt >= MAX_COUNT_DAYS_IN_YEAR) {
            throw IllegalArgumentException("Too many days")
        }

        usedFields.add(Calendar.DAY_OF_MONTH)
        return currentDayOfMonth == nextDayOfMonth
    }
}
