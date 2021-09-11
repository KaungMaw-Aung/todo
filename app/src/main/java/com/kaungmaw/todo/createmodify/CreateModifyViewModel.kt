package com.kaungmaw.todo.createmodify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaungmaw.todo.repositories.NoteRepo
import com.kaungmaw.todo.util.NetworkCallResult
import com.kaungmaw.todo.util.ViewState
import kotlinx.coroutines.launch

class CreateModifyViewModel: ViewModel() {

    private val noteRepo = NoteRepo()

    // creating new item
    private val _addedNewNoteTitleLive = MutableLiveData<ViewState<String>>()
    val addedNewNoteTitleLive: LiveData<ViewState<String>>
        get() = _addedNewNoteTitleLive

    fun addNewNote(note: HashMap<String, Any>) {
        _addedNewNoteTitleLive.value = ViewState.Loading
        viewModelScope.launch {
            noteRepo.addNoteToFireStore(
                note = note
            ).let {
                when (it) {
                    is NetworkCallResult.Success -> {
                        _addedNewNoteTitleLive.value = ViewState.Success(it.data)
                    }
                    is NetworkCallResult.Error -> {
                        _addedNewNoteTitleLive.value = ViewState.Error(it.exception)
                    }
                }
            }
        }
    }

}
