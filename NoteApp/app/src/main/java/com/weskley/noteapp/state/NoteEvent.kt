package com.weskley.noteapp.state

import com.weskley.noteapp.model.NoteEntity

sealed interface NoteEvent {

    data class DeleteNote(val note: NoteEntity): NoteEvent

    data object SaveNote : NoteEvent

    data class GetNoteById(val id: Int) : NoteEvent

    data class UpdateNote(val note: NoteEntity) : NoteEvent

    data class UpdateNoteActiveStatus(val id: Int, val isActive: Boolean) : NoteEvent
}