package ru.ragefalcon.cheatsheetandroid.compose.lifecycles.activities

import android.app.Activity
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.ragefalcon.cheatsheetandroid.R
import ru.ragefalcon.cheatsheetandroid.activity.HelperCommonActivity
import ru.ragefalcon.cheatsheetandroid.activity.HelperTransparentActivity
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TestActivity1(actionLog: (lineText: String, comment: String) -> Unit) {
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }


    val activity = LocalContext.current as Activity

    if (isDialogOpen) {
        val timePickerDialog = TimePickerDialog(
            LocalContext.current,
            { _, hourOfDay, minute ->
                selectedTime = LocalTime.of(hourOfDay, minute)
                isDialogOpen = false
//                    actionLog(
//                        activity.getString(R.string.log_line_alertdialog_close),
//                        activity.getString(R.string.log_comment_alertdialog_close)
//                    )
            },
            selectedTime.hour,
            selectedTime.minute,
            true
        ).apply {
            setOnCancelListener {
                isDialogOpen = false
//                    actionLog(
//                        activity.getString(R.string.log_line_alertdialog_close),
//                        activity.getString(R.string.log_comment_alertdialog_close)
//                    )
            }
            setOnDismissListener {
                isDialogOpen = false
                actionLog(
                    activity.getString(R.string.log_line_alertdialog_close),
                    activity.getString(R.string.log_comment_alertdialog_close)
                )
            }
        }

        DisposableEffect(Unit) {
            timePickerDialog.show()
            onDispose {
                timePickerDialog.dismiss()
            }
        }
    }


    if (showDialog) {
        CustomDialog(onClose = {
            actionLog(
                activity.getString(R.string.log_line_dialog_close),
                activity.getString(R.string.log_comment_dialog_close)
            )
            showDialog = false
        })
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 10.dp)
    ) {

        Text(
            """
                Попробуйте открыть диалоги и новые Activity и посмотрите как их жизненные циклы переплетаются с жизненным циклом текущего Activity.
            """.trimIndent(),
//            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
        )

        TestButton(text = stringResource(
            R.string.button_text_select_time,
            selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        )) {
            actionLog(
                activity.getString(R.string.log_line_alertdialog_open),
                activity.getString(R.string.log_comment_alertdialog_open)
            )
            isDialogOpen = true
        }
        TestButton(activity.getString(R.string.button_text_open_dialog)) {
            actionLog(activity.getString(R.string.log_line_dialog_open), activity.getString(R.string.log_comment_dialog_open))
            showDialog = true
        }
        TestButton(activity.getString(R.string.button_text_open_new_activity)) {
            actionLog(
                activity.getString(R.string.log_line_start_new_activity),
                activity.getString(R.string.log_comment_start_new_activity)
            )
            val intent = Intent(activity, HelperCommonActivity::class.java)
            intent.putExtra("destroyLogging", true)
            intent.putExtra("enableLogging", true)
            activity.startActivity(intent)
        }
        TestButton(activity.getString(R.string.button_text_open_transparent_activity)) {
            actionLog(
                activity.getString(R.string.log_line_start_transparent_activity),
                activity.getString(R.string.log_comment_start_transparent_activity)
            )
            val intent = Intent(activity, HelperTransparentActivity::class.java)
            intent.putExtra("destroyLogging", true)
            intent.putExtra("enableLogging", true)
            activity.startActivity(intent)
        }
        TestButton(activity.getString(R.string.button_text_open_browser)) {
            val url = "https://developer.android.com/guide/components/activities/activity-lifecycle#onpause"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            val pendingIntent = PendingIntent.getActivity(
                activity,
                2432,//NOTIFICATION_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            pendingIntent.send()
        }
    }
}

@Composable
fun TestButton(text: String, onClick: () -> Unit) {
    Button(
        onClick,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiaryContainer,MaterialTheme.colorScheme.onTertiaryContainer),
        elevation = ButtonDefaults.buttonElevation(1.dp),
        modifier = Modifier.fillMaxWidth(0.85f),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text, Modifier.fillMaxWidth())
    }
}


@Composable
fun CustomDialog(onClose: () -> Unit) {
    Dialog(
        onDismissRequest = onClose,
        content = {
            Surface(shape = RoundedCornerShape(5.dp)) {
                Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(R.string.title_simple_dialog))
                    Box(Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Button(onClick = onClose) {
                            Text(stringResource(R.string.button_text_close))
                        }
                    }
                }
            }
        }
    )
}
