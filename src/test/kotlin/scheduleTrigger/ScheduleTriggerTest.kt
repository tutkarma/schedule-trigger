package scheduleTrigger

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

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
        Thread.sleep(60000)
        Assert.assertTrue(executed[0])
    }

    @Test
    fun testPeriodicTask() {
        var counter = 0
        scheduleTrigger!!.scheduleExecution("0 * * * * *", Runnable { counter++ })
        Thread.sleep(120000)
        Assert.assertEquals(2, counter)
    }

    @Test
    fun testOrdinaryTask() {
        val date = Date()
        val calendar = GregorianCalendar()
        calendar.time = date
        calendar.add(Calendar.SECOND, 30)
        val scheduleExpr = "${calendar.get(Calendar.SECOND)} ${calendar.get(Calendar.MINUTE)} " +
                "${calendar.get(Calendar.HOUR_OF_DAY)} ${calendar.get(Calendar.DAY_OF_MONTH)} " +
                "${calendar.get(Calendar.MONTH) + 1} ${calendar.get(Calendar.DAY_OF_WEEK) - 1}"

        var counter = 0
        scheduleTrigger!!.scheduleExecution(scheduleExpr, Runnable { counter++ })
        Thread.sleep(60000)
        Assert.assertEquals(1, counter)
    }
}