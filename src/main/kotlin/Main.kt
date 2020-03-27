package scheduleTrigger

fun main() {
    val trigger = ScheduleTrigger()
    val executed = BooleanArray(1)
    trigger.scheduleExecution("0 * * * * *", Runnable { executed[0] = true })
}