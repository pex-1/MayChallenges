package com.example.maychallenges.ui.undostack

data class Note(val id: Int, val title: String, val subtitle: String)

val initialNotes = listOf(
    Note(1,  "Meeting notes",        "Yesterday, 3:45 PM"),
    Note(2,  "Shopping list",        "Today, 9:12 AM"),
    Note(3,  "Book recommendations", "3 days ago"),
    Note(4,  "Project ideas",        "Last week"),
    Note(5,  "Birthday gift ideas",  "2 days ago"),
    Note(6,  "Weekly goals",         "Monday"),
    Note(7,  "Travel checklist",     "April 30"),
    Note(8,  "Recipe ideas",         "May 1"),
    Note(9,  "Workout plan",         "3 days ago"),
    Note(10, "Reading list",         "Last month")
)
