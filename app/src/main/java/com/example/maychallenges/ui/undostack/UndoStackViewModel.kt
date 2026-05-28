package com.example.minichallenges.challenges.undostack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maychallenges.ui.undostack.Note
import com.example.maychallenges.ui.undostack.initialNotes
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotesUiState(
    val visibleNotes: List<Note> = initialNotes,
    val restoredNoteIds: Set<Int> = emptySet(),
    val pendingCount: Int = 0,
    val showSnackbar: Boolean = false
)

class UndoStackViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private val pendingDeletion = mutableListOf<Note>()
    private var snackbarJob: Job? = null

    fun onDeleteNote(note: Note) {
        _uiState.update { state -> state.copy(visibleNotes = state.visibleNotes.filter { it.id != note.id }) }
        pendingDeletion.add(note)
        snackbarJob?.cancel()
        launchSnackbarJob()
    }

    fun onUndo() {
        snackbarJob?.cancel()
        snackbarJob = null
        if (pendingDeletion.isEmpty()) return
        val toRestore = pendingDeletion.toList()
        pendingDeletion.clear()
        _uiState.update { state ->
            state.copy(
                visibleNotes = (state.visibleNotes + toRestore).sortedBy { it.id },
                restoredNoteIds = toRestore.map { it.id }.toSet(),
                pendingCount = 0,
                showSnackbar = false
            )
        }
        viewModelScope.launch {
            delay(2_000L)
            _uiState.update { it.copy(restoredNoteIds = emptySet()) }
        }
    }

    fun onSnackbarDismissed() {
        pendingDeletion.clear()
        _uiState.update { it.copy(pendingCount = 0, showSnackbar = false) }
    }

    private fun launchSnackbarJob() {
        snackbarJob = viewModelScope.launch {
            _uiState.update { state -> state.copy(pendingCount = pendingDeletion.size, showSnackbar = true) }
        }
    }
}
