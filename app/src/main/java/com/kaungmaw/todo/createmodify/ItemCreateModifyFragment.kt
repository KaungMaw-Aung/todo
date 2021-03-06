package com.kaungmaw.todo.createmodify

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kaungmaw.todo.R
import com.kaungmaw.todo.databinding.FragmentItemCreateModifyBinding
import com.kaungmaw.todo.domain.Note
import com.kaungmaw.todo.extensions.setNavResult
import com.kaungmaw.todo.home.FROM_ITEM_CREATED
import com.kaungmaw.todo.util.LoadingDialog
import com.kaungmaw.todo.util.ViewState
import java.util.*

class ItemCreateModifyFragment : Fragment() {

    private lateinit var binding: FragmentItemCreateModifyBinding
    private val viewModel by viewModels<CreateModifyViewModel>()
    private val navArgs by navArgs<ItemCreateModifyFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentItemCreateModifyBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navArgs.noteId?.let {
            binding.btnSubmitEntry.isVisible = false
            binding.btnCancelEntry.isVisible = false
            binding.tieTitle.isEnabled = false
            binding.tieNote.isEnabled = false

            viewModel.getNote(it)
        }

        binding.btnSubmitEntry.setOnClickListener {
            if (areAllTextFieldsNotEmpty()) {
                viewModel.addNewNote(
                    note = hashMapOf(
                        "title" to binding.tieTitle.text.toString(),
                        "note" to binding.tieNote.text.toString(),
                        "created_at" to Timestamp.now()
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
                        setNavResult(FROM_ITEM_CREATED, true)
                        popBackStack()
                    }
                }
                is ViewState.Error -> {
                    LoadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.noteLive.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.gpContainer.isVisible = false
                    binding.pbLoadingCircle.isVisible = true
                }
                is ViewState.Success -> {
                    binding.gpContainer.isVisible = true
                    binding.pbLoadingCircle.isVisible = false
                    binding.tieTitle.setText(it.data.title)
                    binding.tieNote.setText(it.data.note)

                    viewModel.previousNote = Note(
                        id = it.data.id,
                        title = it.data.title,
                        note = it.data.note,
                        createdAt = it.data.createdAt

                    )
                }
                is ViewState.Error -> {
                    binding.gpContainer.isVisible = true
                    binding.pbLoadingCircle.isVisible = false
                    Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.deleteProgressLive.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    LoadingDialog.show(requireContext())
                }
                is ViewState.Success -> {
                    LoadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.data, Toast.LENGTH_LONG).show()

                    findNavController().apply {
                        setNavResult(FROM_ITEM_CREATED, true)
                        popBackStack()
                    }
                }
                is ViewState.Error -> {
                    LoadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.updateProgressLive.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    LoadingDialog.show(requireContext())
                }
                is ViewState.Success -> {
                    LoadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.data, Toast.LENGTH_LONG).show()

                    findNavController().apply {
                        setNavResult(FROM_ITEM_CREATED, true)
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

        binding.btnCancelUpdate.setOnClickListener {
            binding.tieTitle.isEnabled = false
            binding.tieNote.isEnabled = false

            binding.btnSubmitUpdate.isVisible = false
            binding.btnCancelUpdate.isVisible = false

            binding.tieTitle.setText(viewModel.previousNote!!.title)
            binding.tieNote.setText(viewModel.previousNote!!.note)
        }

        binding.btnSubmitUpdate.setOnClickListener {

            val updatedNote = Note(
                id = navArgs.noteId!!,
                title = binding.tieTitle.text.toString(),
                note = binding.tieNote.text.toString(),
                createdAt = viewModel.previousNote!!.createdAt
            )

            if (areTwoNotesNotTheSame(twoNotes = viewModel.previousNote!! to updatedNote)) {
                viewModel.updateNote(
                    navArgs.noteId!!,
                    hashMapOf(
                        "title" to binding.tieTitle.text.toString(),
                        "note" to binding.tieNote.text.toString(),
                        "created_at" to viewModel.previousNote!!.createdAt
                    )
                )
            } else {
                Toast.makeText(requireContext(), "Should not be the same value!", Toast.LENGTH_LONG)
                    .show()
            }
        }

        setHasOptionsMenu(navArgs.noteId != null)

    }

    private fun areTwoNotesNotTheSame(twoNotes: Pair<Note, Note>) =
        twoNotes.first != twoNotes.second

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.modify_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_update -> {
                binding.btnCancelUpdate.isVisible = true
                binding.btnSubmitUpdate.isVisible = true

                binding.tieTitle.isEnabled = true
                binding.tieNote.isEnabled = true
            }
            R.id.action_delete -> viewModel.deleteNote(navArgs.noteId!!)
        }
        return true
    }

}