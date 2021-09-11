package com.kaungmaw.todo.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

}