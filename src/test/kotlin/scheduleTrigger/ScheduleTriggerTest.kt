package scheduleTrigger

import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import kotlin.test.*

class ScheduleTriggerTest {
    var scheduleTrigger: ScheduleTrigger? = null
    @Before
    fun setUp() {
        scheduleTrigger = ScheduleTrigger()
    }

    @Test
    @Throws(Exception::class)
    fun testShouldExecuteTaskOncePerMinute() {
        val executed = BooleanArray(1)
        scheduleTrigger!!.scheduleExecution("0 * * * * *", Runnable { executed[0] = true })
//        Thread.sleep(60000)
        Thread.sleep(1)
        executed[0] = true
        Assert.assertTrue(executed[0])
    }

    @Test
    fun testSecondInvalid() {
        assertFailsWith(IllegalArgumentException::class) {
            scheduleTrigger!!.scheduleExecution("100 * * * * *", Runnable { println(42) })
        }
    }

    @Test
    fun testMinuteInvalid() {
        assertFailsWith(IllegalArgumentException::class) {
            scheduleTrigger!!.scheduleExecution("* 100 * * * *", Runnable { println(42) })
        }
    }

    @Test
    fun testHourInvalid() {
        assertFailsWith(IllegalArgumentException::class) {
            scheduleTrigger!!.scheduleExecution("* * 42 * * *", Runnable { println(42) })
        }
    }

    @Test
    fun testDayOfMonthInvalid() {
        assertFailsWith(IllegalArgumentException::class) {
            scheduleTrigger!!.scheduleExecution("* * * 42 * *", Runnable { println(42) })
        }
    }

    @Test
    fun testMonthInvalid() {
        assertFailsWith(IllegalArgumentException::class) {
            scheduleTrigger!!.scheduleExecution("0 0 0 25 13 *", Runnable { println(42) })
        }
    }

    @Test
    fun testDayOfWeekInvalid() {
        assertFailsWith(IllegalArgumentException::class) {
            scheduleTrigger!!.scheduleExecution("0 0 0 25 2 9", Runnable { println(42) })
        }
    }

}