package com.example.maychallenges.ui.undostack

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.maychallenges.ui.theme.ErrorContainter
import com.example.maychallenges.ui.theme.MayChallengesTheme
import com.example.minichallenges.challenges.undostack.NotesUiState
import com.example.minichallenges.challenges.undostack.UndoStackViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private val RestoredHighlight = Color(0xFFE8F5E9)

// ── Stateful entry point ──────────────────────────────────────────────────────

@Composable
fun NotesScreen(
    onBack: () -> Unit = {},
    viewModel: UndoStackViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var activeSnackbarJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(uiState.showSnackbar, uiState.pendingCount) {
        if (!uiState.showSnackbar) return@LaunchedEffect
        activeSnackbarJob?.cancel()
        snackbarHostState.currentSnackbarData?.dismiss()
        activeSnackbarJob = coroutineScope.launch {
            val count = uiState.pendingCount
            val result = snackbarHostState.showSnackbar(
                message = if (count == 1) "1 item deleted" else "$count items deleted",
                actionLabel = "Undo",
                duration = SnackbarDuration.Long
            )
            when (result) {
                SnackbarResult.ActionPerformed -> viewModel.onUndo()
                SnackbarResult.Dismissed -> viewModel.onSnackbarDismissed()
            }
        }
    }

    NotesContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onDelete = viewModel::onDeleteNote
    )
}

// ── Stateless content ─────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesContent(
    uiState: NotesUiState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onDelete: (Note) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Notes", fontWeight = FontWeight.Bold, fontSize = 20.sp) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(uiState.visibleNotes, key = { it.id }) { note ->
                NoteItem(
                    note = note,
                    isRestored = note.id in uiState.restoredNoteIds,
                    onDelete = { onDelete(note) }
                )
            }
        }
    }
}

@Composable
fun NoteItem(note: Note, isRestored: Boolean, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isRestored) RestoredHighlight else MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(note.title, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(
                note.subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .size(40.dp)
                .background(ErrorContainter, shape = RoundedCornerShape(8.dp))
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(name = "Full Notes List", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewFullList() {
    MayChallengesTheme {
        NotesContent(uiState = NotesUiState(), onDelete = {})
    }
}

@Preview(name = "Partially Deleted (3 gone)", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewPartiallyDeleted() {
    MayChallengesTheme {
        NotesContent(
            uiState = NotesUiState(
                visibleNotes = initialNotes.drop(3),
                pendingCount = 3,
                showSnackbar = true
            ),
            onDelete = {}
        )
    }
}

@Preview(name = "Restored Highlighted", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewRestored() {
    MayChallengesTheme {
        NotesContent(
            uiState = NotesUiState(restoredNoteIds = setOf(1, 2, 3)),
            onDelete = {}
        )
    }
}

@Preview(name = "Note Item – Default", showBackground = true, widthDp = 360)
@Composable
private fun PreviewNoteItemDefault() {
    MayChallengesTheme {
        Surface {
            NoteItem(
                note = Note(1, "Meeting notes", "Yesterday, 3:45 PM"),
                isRestored = false,
                onDelete = {})
        }
    }
}

@Preview(name = "Note Item – Restored", showBackground = true, widthDp = 360)
@Composable
private fun PreviewNoteItemRestored() {
    MayChallengesTheme {
        Surface {
            NoteItem(
                note = Note(1, "Meeting notes", "Yesterday, 3:45 PM"),
                isRestored = true,
                onDelete = {})
        }
    }
}
