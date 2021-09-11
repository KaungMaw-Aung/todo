package com.kaungmaw.todo.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kaungmaw.todo.databinding.ItemRecyclerNoteBinding
import com.kaungmaw.todo.domain.Note

class TodoListAdapter() : ListAdapter<Note, NoteViewHolder>(NoteDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            ItemRecyclerNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

object NoteDiffUtil : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}

class NoteViewHolder(private val binding: ItemRecyclerNoteBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Note) {
        binding.tvItemTitle.text = data.title
        binding.tvItemNote.text = data.note
    }

}