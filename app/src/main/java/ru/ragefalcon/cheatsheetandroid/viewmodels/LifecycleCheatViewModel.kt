package ru.ragefalcon.cheatsheetandroid.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.ragefalcon.cheatsheetandroid.compose.lifecycles.LifecyclesCheatList
import javax.inject.Inject

@HiltViewModel
class LifecycleCheatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val cheat = LifecyclesCheatList.getCheat(savedStateHandle.get<String>(LIFECYCLE_CHEAT_SAVED_STATE_KEY) ?: "")

    companion object {
        const val LIFECYCLE_CHEAT_SAVED_STATE_KEY = "lifecycleCheatKey"
    }
}