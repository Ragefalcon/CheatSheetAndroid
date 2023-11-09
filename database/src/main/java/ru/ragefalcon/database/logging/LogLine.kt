import ru.ragefalcon.database.MainLog
import ru.ragefalcon.database.logging.LogApi

data class LogLine(
    val id: Long,
    val date: Long,
    val tagTitle: String,
    val logText: String,
    val comment: String
){
    fun tag(): LogApi.Tag = LogApi.Tag.getTag(tagTitle)
    fun logMessage(): String = "${tag().logText} - $logText"

    constructor(mainLog: MainLog): this(
        id = mainLog._id,
        date = mainLog.date,
        tagTitle = mainLog.tag,
        logText = mainLog.log_text,
        comment = mainLog.comment
    )
}