package com.kaungmaw.todo.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kaungmaw.todo.databinding.FragmentTodoListBinding
import com.kaungmaw.todo.extensions.getNavResult
import com.kaungmaw.todo.util.ViewState

const val FROM_ITEM_CREATED = "ITEM_CREATED"

class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding
    private val viewModel by viewModels<TodoListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentTodoListBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().getNavResult<Boolean>(FROM_ITEM_CREATED)?.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.getNotes()
                findNavController().currentBackStackEntry?.savedStateHandle?.set(
                    FROM_ITEM_CREATED,
                    false
                )
            }
        }

        val adapter = TodoListAdapter { id ->
            findNavController().navigate(TodoListFragmentDirections.toNoteCreateModify(id))
        }

        binding.rvNotes.adapter = adapter

        viewModel.notesLive.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.pbLoadingCircle.isVisible = true
                    binding.rvNotes.isVisible = false
                    binding.fabAddNewTodo.hide()
                }
                is ViewState.Success -> {
                    binding.pbLoadingCircle.isVisible = false
                    binding.rvNotes.isVisible = true
                    binding.fabAddNewTodo.show()
                    adapter.submitList(it.data)
                }
                is ViewState.Error -> {
                    binding.pbLoadingCircle.isVisible = false
                    binding.fabAddNewTodo.show()
                    Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
                }
            }
        }


        binding.fabAddNewTodo.setOnClickListener {
            findNavController().navigate(
                TodoListFragmentDirections.toNoteCreateModify(null)
            )
        }
    }

}