package ru.ragefalcon.cheatsheetandroid.compose.lifecycles.activities

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

enum class TestListForActivity(val number: Int, val title: String) {
    TEST1(1, "Dialogs, new Activity"),
    TEST2(2, "Взаимодействия"),
    TEST3(3, "Принудительное завершение");
}

@Composable
fun TestListForActivity.openContent(
    scope: ColumnScope,
    actionLog: (lineText: String, comment: String) -> Unit
) {
    scope.apply {
        when (this@openContent) {
            TestListForActivity.TEST1 -> {
                TestActivity1(actionLog)
            }

            TestListForActivity.TEST2 -> {
                TestActivity2()
            }

            TestListForActivity.TEST3 -> {
                TestActivity3(actionLog)
            }
        }
    }
}
