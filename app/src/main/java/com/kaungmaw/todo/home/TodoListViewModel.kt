package com.kaungmaw.todo.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaungmaw.todo.domain.Note
import com.kaungmaw.todo.repositories.NoteRepo
import com.kaungmaw.todo.util.NetworkCallResult
import com.kaungmaw.todo.util.ViewState
import kotlinx.coroutines.launch

class TodoListViewModel: ViewModel() {

    private val noteRepo = NoteRepo()

    private val _notesLive = MutableLiveData<ViewState<List<Note>>>()
    val notesLive: LiveData<ViewState<List<Note>>>
        get() = _notesLive

    fun getNotes() {
        viewModelScope.launch {
            _notesLive.value = ViewState.Loading
            noteRepo.getNotesFromFireStore().let {
                when (it) {
                    is NetworkCallResult.Success -> {
                        _notesLive.value = ViewState.Success(it.data)
                    }
                    is NetworkCallResult.Error -> {
                        _notesLive.value = ViewState.Error(it.exception)
                    }
                }
            }
        }
    }

    init {
        getNotes()
    }

}