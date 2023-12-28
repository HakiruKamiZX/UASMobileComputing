package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import android.content.Context

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addNoteButton: Button
    private lateinit var noteAdapter: NoteAdapter

    private val notes = mutableListOf(
        Note("Example Note", "This is an example content for your future note :)"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        addNoteButton = findViewById(R.id.addNoteButton)

        noteAdapter = NoteAdapter(notes,
            onItemClick = { selectedNote ->
                openEditNoteDialog(selectedNote)
            },
            onDeleteClick = { selectedNote ->
                deleteNote(selectedNote)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = noteAdapter

        addNoteButton.setOnClickListener {
            val newNote = Note("Untitled Note", "")
            notes.add(newNote)
            noteAdapter.notifyDataSetChanged()
        }
    }

    private fun openEditNoteDialog(note: Note) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_edit_note, null)

        val titleEditText: EditText = dialogView.findViewById(R.id.titleEditText)
        val contentEditText: EditText = dialogView.findViewById(R.id.contentEditText)

        titleEditText.setText(note.title)
        contentEditText.setText(note.content)

        builder.setView(dialogView)
            .setTitle("Edit Note")
            .setPositiveButton("Save") { _, _ ->
                val editedNote = Note(
                    titleEditText.text.toString(),
                    contentEditText.text.toString()
                )
                updateNoteInList(note, editedNote)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateNoteInList(oldNote: Note, newNote: Note) {
        val position = notes.indexOf(oldNote)
        if (position != -1) {
            notes[position] = newNote
            noteAdapter.notifyItemChanged(position)
        }
    }

    private var isDeleteDialogVisible = false

    private fun deleteNote(note: Note) {
        if (!isDeleteDialogVisible) {
            showDeleteConfirmationDialog(this, note)
        }
    }

    private fun showDeleteConfirmationDialog(context: Context, note: Note) {
        AlertDialog.Builder(context)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { _, _ ->
                performDeleteNoteAction(note)
                isDeleteDialogVisible = false
            }
            .setNegativeButton("No") { _, _ ->
                isDeleteDialogVisible = false
            }
            .setOnDismissListener {
                isDeleteDialogVisible = false
            }
            .show()

        isDeleteDialogVisible = true
    }

    private fun performDeleteNoteAction(note: Note) {
        notes.remove(note)
        noteAdapter.notifyDataSetChanged()
    }

}


