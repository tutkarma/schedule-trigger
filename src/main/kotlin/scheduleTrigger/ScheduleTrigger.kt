package scheduleTrigger

import java.util.*
import kotlin.concurrent.timerTask

class ScheduleTrigger {
    private val timer = Timer(true)

    fun scheduleExecution(schedule: String, task: Runnable) {
        val scheduleExpr = ScheduleExpression(schedule)
        val scheduleTask = ScheduleTask(scheduleExpr, task)

        if (scheduleTask.isPeriodic) {
            timer.schedule(timerTask {
                scheduleTask.run()
            }, scheduleTask.nextExecutionTime(), scheduleTask.period())
        } else {
            timer.schedule(timerTask {
                scheduleTask.run()
            }, scheduleTask.nextExecutionTime())
        }
    }
}
