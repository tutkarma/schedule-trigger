package scheduleTrigger

import java.util.*

class ScheduleTask(private val expression: ScheduleExpression, private val task: Runnable) {

    val isPeriodic: Boolean
        get() = expression.isPeriodic

    fun run() {
        task.run()
    }

    fun nextExecutionTime(): Date {
        val date = Date()
        return expression.next(date)
    }

    fun period(): Long {
        val date = Date()
        val firstExecution = expression.next(date)
        val secondExecution = expression.next(firstExecution)
        return secondExecution.time - firstExecution.time
    }
}
