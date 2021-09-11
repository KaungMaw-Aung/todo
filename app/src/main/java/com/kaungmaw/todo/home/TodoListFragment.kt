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
import com.kaungmaw.todo.R
import com.kaungmaw.todo.databinding.FragmentTodoListBinding
import com.kaungmaw.todo.util.LoadingDialog
import com.kaungmaw.todo.util.ViewState

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

        viewModel.notesLive.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.pbLoadingCircle.isVisible = true
                    binding.fabAddNewTodo.hide()
                }
                is ViewState.Success -> {
                    binding.pbLoadingCircle.isVisible = false
                    binding.fabAddNewTodo.show()
                    Toast.makeText(
                        requireContext(),
                        it.data.size.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
                is ViewState.Error -> {
                    binding.pbLoadingCircle.isVisible = false
                    binding.fabAddNewTodo.show()
                    Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
                }
            }
        }


        binding.fabAddNewTodo.setOnClickListener {
            findNavController().navigate(R.id.itemCreateModifyFragment)
        }
    }

}