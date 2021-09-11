package com.kaungmaw.todo.createmodify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kaungmaw.todo.R
import com.kaungmaw.todo.databinding.FragmentItemCreateModifyBinding
import com.kaungmaw.todo.extensions.setNavResult
import com.kaungmaw.todo.home.ITEM_CREATED
import com.kaungmaw.todo.util.LoadingDialog
import com.kaungmaw.todo.util.ViewState

class ItemCreateModifyFragment : Fragment() {

    private lateinit var binding: FragmentItemCreateModifyBinding
    private val viewModel by viewModels<CreateModifyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentItemCreateModifyBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmitEntry.setOnClickListener {
            if (areAllTextFieldsNotEmpty()) {
                viewModel.addNewNote(
                    note = hashMapOf(
                        "title" to binding.tieTitle.text.toString(),
                        "note" to binding.tieNote.text.toString()
                    )
                )
            }
        }

        viewModel.addedNewNoteTitleLive.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    LoadingDialog.show(requireContext())
                }
                is ViewState.Success -> {
                    LoadingDialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.note_create_success, it.data),
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().apply {
                        setNavResult(ITEM_CREATED, true)
                        popBackStack()
                    }
                }
                is ViewState.Error -> {
                    LoadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btnCancelEntry.setOnClickListener { findNavController().popBackStack() }

    }

    private fun areAllTextFieldsNotEmpty(): Boolean {

        if (binding.tieTitle.text.isNullOrBlank()) {
            binding.tilTitle.error = getString(R.string.required_input)
        } else {
            binding.tilTitle.error = null
        }

        if (binding.tieNote.text.isNullOrBlank()) {
            binding.tilNote.error = getString(R.string.required_input)
        } else {
            binding.tilNote.error = null
        }

        return (binding.tieTitle.text!!.isNotBlank() && binding.tieNote.text!!.isNotBlank())
    }

}