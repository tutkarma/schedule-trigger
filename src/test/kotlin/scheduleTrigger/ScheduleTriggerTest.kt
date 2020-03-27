package scheduleTrigger

import org.junit.Assert
import org.junit.Before
import org.junit.Test

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
}