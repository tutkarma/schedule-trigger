package scheduleTrigger

import org.junit.Test
import java.util.*
import kotlin.test.*

class ScheduleExpressionTest {

    @Test
    fun testEmptyStringExpression() {
        assertFailsWith(IllegalArgumentException::class) {
            ScheduleExpression("")
        }
    }

    @Test
    fun testLengthInvalid() {
        assertFailsWith(IllegalArgumentException::class) {
            ScheduleExpression("15 30 * * *")
        }
        assertFailsWith(IllegalArgumentException::class) {
            ScheduleExpression("1 1 1 1 1 1 1")
        }
    }

    @Test
    fun testSecondInvalid() {
        assertFailsWith(IllegalArgumentException::class) {
            ScheduleExpression("100 * * * * *")
        }
        assertFailsWith(IllegalArgumentException::class) {
            ScheduleExpression("-2 * * * * *")
        }
    }

    @Test
    fun testMinuteInvalid() {
        assertFailsWith(IllegalArgumentException::class) {
            ScheduleExpression("* 100 * * * *")
        }
        assertFailsWith(IllegalArgumentException::class) {
            ScheduleExpression("* -2 * * * *")
        }
    }

    @Test
    fun testHourInvalid() {
        assertFailsWith(IllegalArgumentException::class) {
            ScheduleExpression("* * 42 * * *")
        }
        assertFailsWith(IllegalArgumentException::class) {
            ScheduleExpression("* * -2 * * *")
        }
    }

    @Test
    fun testDayOfMonthInvalid() {
        assertFailsWith(IllegalArgumentException::class) {
            ScheduleExpression("* * * 42 * *")
        }
        assertFailsWith(IllegalArgumentException::class) {
            ScheduleExpression("* * * 0 * *")
        }
    }

    @Test
    fun testMonthInvalid() {
        assertFailsWith(IllegalArgumentException::class) {
            ScheduleExpression("0 0 0 25 13 *")
        }
        assertFailsWith(IllegalArgumentException::class) {
            ScheduleExpression("0 0 0 -10 13 *")
        }
    }

    @Test
    fun testDayOfWeekInvalid() {
        assertFailsWith(IllegalArgumentException::class) {
            ScheduleExpression("0 0 0 25 2 7")
        }
        assertFailsWith(IllegalArgumentException::class) {
            ScheduleExpression("0 0 0 25 2 -7")
        }
    }

    @Test
    fun testDateExecutionEverySecond() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 9, 42, 50)
        val curDate = calendar.time
        calendar.set(2020, 3, 1, 9, 42, 50)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("* * * * * *").next(curDate))
    }

    @Test
    fun testDateExecutionSecondCurrentMinute() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 9, 42, 10)
        val curDate = calendar.time
        calendar.set(2020, 3, 1, 9, 42, 15)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("15 * * * * *").next(curDate))
    }

    @Test
    fun testDateExecutionSecondNextMinute() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 9, 42, 58)
        val curDate = calendar.time
        calendar.set(2020, 3, 1, 9, 43, 15)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("15 * * * * *").next(curDate))
    }

    @Test
    fun testDateExecutionEveryMinute() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 9, 42, 30)
        val curDate = calendar.time
        calendar.set(2020, 3, 1, 9, 42, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("30 * * * * *").next(curDate))
    }

    @Test
    fun testDateExecutionOnlyMinute() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 9, 42, 30)
        val curDate = calendar.time
        calendar.set(2020, 3, 1, 9, 42, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("* 42 * * * *").next(curDate))
    }

    @Test
    fun testDateExecutionMinuteCurrentHour() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 9, 13, 30)
        val curDate = calendar.time
        calendar.set(2020, 3, 1, 9, 15, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("30 15 * * * *").next(curDate))
    }

    @Test
    fun testDateExecutionMinuteNextHour() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 9, 42, 30)
        val curDate = calendar.time
        calendar.set(2020, 3, 1, 10, 15, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("30 15 * * * *").next(curDate))
    }

    @Test
    fun testDateExecutionEveryHour() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 9, 42, 30)
        val curDate = calendar.time
        calendar.set(2020, 3, 1, 9, 42, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("30 42 * * * *").next(curDate))
    }

    @Test
    fun testDateExecutionOnlyHour() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 9, 42, 30)
        val curDate = calendar.time
        calendar.set(2020, 3, 1, 9, 42, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("* * 9 * * *").next(curDate))
    }

    @Test
    fun testDateExecutionHourCurrentDay() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 7, 0, 0)
        val curDate = calendar.time
        calendar.set(2020, 3, 1, 9, 0, 0)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("0 0 9 * * *").next(curDate))
    }

    @Test
    fun testDateExecutionHourNextDay() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 15, 0, 0)
        val curDate = calendar.time
        calendar.set(2020, 3, 2, 9, 0, 0)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("0 0 9 * * *").next(curDate))
    }

    @Test
    fun testDateExecutionEveryDay() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 9, 42, 30)
        val curDate = calendar.time
        calendar.set(2020, 3, 1, 9, 42, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("30 42 9 * * *").next(curDate))
    }

    @Test
    fun testDateExecutionOnlyDay() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 9, 42, 30)
        val curDate = calendar.time
        calendar.set(2020, 3, 1, 9, 42, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("* * * 1 * *").next(curDate))
    }

    @Test
    fun testDateExecutionDayCurrentMonth() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 9, 42, 30)
        val curDate = calendar.time
        calendar.set(2020, 3, 10, 9, 42, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("30 42 9 10 * *").next(curDate))
    }

    @Test
    fun testDateExecutionDayNextMonth() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 15, 9, 42, 30)
        val curDate = calendar.time
        calendar.set(2020, 4, 10, 9, 42, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("30 42 9 10 * *").next(curDate))
    }

    @Test
    fun testDateExecutionDayShortMonth() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 15, 9, 42, 30)
        val curDate = calendar.time
        calendar.set(2020, 4, 31, 9, 42, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("30 42 9 31 * *").next(curDate))
    }

    @Test
    fun testDateExecutionDayLongMonth() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 4, 31, 9, 42, 30)
        val curDate = calendar.time
        calendar.set(2020, 4, 31, 9, 42, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("30 42 9 31 * *").next(curDate))
    }

    @Test
    fun testDateExecutionEveryMonth() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 9, 42, 30)
        val curDate = calendar.time
        calendar.set(2020, 3, 1, 9, 42, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("30 42 9 1 * *").next(curDate))
    }

    @Test
    fun testDateExecutionOnlyMonth() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 3, 1, 9, 42, 30)
        val curDate = calendar.time
        calendar.set(2020, 3, 1, 9, 42, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("* * * * 4 *").next(curDate))
    }

    @Test
    fun testDateExecutionMonthCurrentYear() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 1, 1, 9, 42, 30)
        val curDate = calendar.time
        calendar.set(2020, 3, 1, 9, 42, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("30 42 9 1 4 *").next(curDate))
    }

    @Test
    fun testDateExecutionMonthNextYear() {
        val calendar = GregorianCalendar()
        calendar.set(2020, 9, 1, 9, 42, 30)
        val curDate = calendar.time
        calendar.set(2021, 3, 1, 9, 42, 30)
        val nextDate = calendar.time
        equalDate(nextDate, ScheduleExpression("30 42 9 1 4 *").next(curDate))
    }

    private fun equalDate(expectedDate: Date, actualDate: Date) {
        val expected = GregorianCalendar()
        val actual = GregorianCalendar()
        expected.time = expectedDate
        actual.time = actualDate
        assertEquals(expected.get(Calendar.YEAR), actual.get(Calendar.YEAR))
        assertEquals(expected.get(Calendar.MONTH), actual.get(Calendar.MONTH))
        assertEquals(expected.get(Calendar.DAY_OF_MONTH), actual.get(Calendar.DAY_OF_MONTH))
        assertEquals(expected.get(Calendar.HOUR_OF_DAY), actual.get(Calendar.HOUR_OF_DAY))
        assertEquals(expected.get(Calendar.MINUTE), actual.get(Calendar.MINUTE))
        assertEquals(expected.get(Calendar.SECOND), actual.get(Calendar.SECOND))
    }
}

