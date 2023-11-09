package ru.ragefalcon.cheatsheetandroid.compose.lifecycles.activities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TestActivity2() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
    ) {

        Text(
            """
                Для удобства рекомендуется перед каждым новым тестом или очищать Logs или выводить в Logs разделительную полосу.
                
                - Попробуйте повернуть устройство и посмотрите как методы Activity будут вызваны.
                
                - Попробуйте закрыть текущую Activity кнопкой назад или кнопкой на TopAppBar и потом снова сюда вернуться.
                
                - Попробуйте свернуть приложение и потом вернуться обратно.
                
                - Попробуйте свернуть приложение, потом закрыть его из менеджера задач, а потом снова запустить приложение (если текущее Activity было в стеке задач, то приложение при старте откроет его, дополнительные Activity из тестов воссоздаваться не будут).
                 
                - Попробуйте повторить все эти тесты с дополнительной Activity, которую можно открыть в разделе "Test1". 
            """.trimIndent(),
//            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
        )
    }
}