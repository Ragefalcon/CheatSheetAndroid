package ru.ragefalcon.cheatsheetandroid.compose.lifecycles

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ru.ragefalcon.cheatsheetandroid.R
import ru.ragefalcon.cheatsheetandroid.di.OrientationState
import ru.ragefalcon.cheatsheetandroid.viewmodels.LifecycleCheatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifecycleCheatOpenScreen(
    orientationState: OrientationState,
    viewModel: LifecycleCheatViewModel = hiltViewModel(),
    onBackNavigation: () -> Unit
) {
    Scaffold(modifier = Modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onBackNavigation) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.content_description_navigation_up)
                            )
                        }
                        Text(viewModel.cheat.title)
                    }
                },
            )
        }
    ) {
        viewModel.cheat.getScreen(Modifier.padding(it).fillMaxSize())
    }
}