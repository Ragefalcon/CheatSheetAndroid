package ru.ragefalcon.cheatsheetandroid.compose.lifecycles.activities

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ru.ragefalcon.cheatsheetandroid.R
import ru.ragefalcon.cheatsheetandroid.activity.HelperCommonActivity

@Composable
fun TestActivity3(actionLog: (lineText: String, comment: String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 10.dp)
    ) {

        val activity = LocalContext.current as Activity

        Text(
            """
                Кнопки ниже запускают новую Activity, но в ней выполняется метод finish() в onCreate и в onStart соответственно.
            """.trimIndent(),
//            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
        )

        TestButton(activity.getString(R.string.button_text_open_new_activity_with_finish_on_create)) {
            actionLog(
                activity.getString(R.string.log_line_start_new_activity),
                activity.getString(R.string.log_comment_start_new_activity)
            )
            val intent = Intent(activity, HelperCommonActivity::class.java)
            intent.putExtra("destroyLogging", true)
            intent.putExtra("enableLogging", true)
            intent.putExtra("destroyInOnCreate", true)
            activity.startActivity(intent)
        }

        TestButton(activity.getString(R.string.button_text_open_new_activity_with_finish_on_start)) {
            actionLog(
                activity.getString(R.string.log_line_start_new_activity),
                activity.getString(R.string.log_comment_start_new_activity)
            )
            val intent = Intent(activity, HelperCommonActivity::class.java)
            intent.putExtra("destroyLogging", true)
            intent.putExtra("enableLogging", true)
            intent.putExtra("destroyInOnStart", true)
            activity.startActivity(intent)
        }

    }
}