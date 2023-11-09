package ru.ragefalcon.cheatsheetandroid.compose.lifecycles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ragefalcon.cheatsheetandroid.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifecycleCheatItem(item: LifecyclesCheatList, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.card_side_margin))
            .padding(vertical = dimensionResource(R.dimen.card_bottom_margin)),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurface),
        elevation = CardDefaults.elevatedCardElevation(2.dp, 0.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(item.title,Modifier.padding(bottom = 12.dp), style = MaterialTheme.typography.bodyLarge)
        }
    }
}