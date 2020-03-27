package scheduleTrigger

import java.util.*

class ScheduleTask(expr: ScheduleExpression, task: Runnable) {

    val task = task
    val scheduleExpr = expr

    fun isPeriodic(): Boolean {
        return true;
    }

    fun run() {
        task.run()
    }

    fun nextExecutionTime(): Date {
        val date = Date()
        return date
    }
}
