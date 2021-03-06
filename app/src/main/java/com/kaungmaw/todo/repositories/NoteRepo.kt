package com.kaungmaw.todo.repositories

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kaungmaw.todo.domain.Note
import com.kaungmaw.todo.util.NetworkCallResult
import kotlinx.coroutines.tasks.await

class NoteRepo {

    private val db = Firebase.firestore

    // add or create note
    suspend fun addNoteToFireStore(note: HashMap<String, Any>): NetworkCallResult<String> {
        // Add a new note with a generated ID
        return try {
            db.collection("notes")
                .add(note)
                .await()
                .get()
                .await()
                .get("title").toString().let { NetworkCallResult.Success(it) }
        } catch (e: Exception) {
            NetworkCallResult.Error(e)
        }
    }

    // get notes
    suspend fun getNotesFromFireStore(): NetworkCallResult<List<Note>> {
        return try {
            db.collection("notes")
                .orderBy("created_at")
                .get()
                .await()
                .documents.map {
                    Note(
                        id = it.id,
                        title = it["title"].toString(),
                        note = it["note"].toString(),
                        createdAt = it["created_at"] as Timestamp
                    )
                }.let { NetworkCallResult.Success(it) }
        } catch (e: Exception) {
            NetworkCallResult.Error(e)
        }
    }

    // get single note
    suspend fun getNoteFromFireStore(noteId: String): NetworkCallResult<Note> {
        return try {
            db.collection("notes").document(noteId)
                .get()
                .await()
                .data.let {
                    NetworkCallResult.Success(
                        Note(
                            id = noteId,
                            title = it?.get("title")?.toString().orEmpty(),
                            note = it?.get("note")?.toString().orEmpty(),
                            createdAt = it?.get("created_at") as Timestamp
                        )
                    )
                }
        } catch (e: Exception) {
            NetworkCallResult.Error(e)
        }
    }

    // delete note
    suspend fun deleteNoteFromFireStore(noteId: String): NetworkCallResult<String> {
        return try {
            db.collection("notes").document(noteId)
                .delete()
                .await()
            NetworkCallResult.Success("Successfully Deleted!")
        } catch (e: Exception) {
            NetworkCallResult.Error(e)
        }
    }

    // update note
    suspend fun updateNoteFromFireStore(
        noteId: String,
        note: HashMap<String, Any>
    ): NetworkCallResult<String> {
        return try {
            db.collection("notes").document(noteId)
                .set(note)
                .await()
            NetworkCallResult.Success("Successfully Updated!")
        } catch (e: Exception) {
            NetworkCallResult.Error(e)
        }
    }

}