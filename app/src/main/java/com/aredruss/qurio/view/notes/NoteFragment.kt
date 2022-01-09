package com.aredruss.qurio.view.notes

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aredruss.qurio.R
import com.aredruss.qurio.databinding.FragmentNoteBinding
import com.aredruss.qurio.model.LiteNote
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.view.MainActivity
import com.aredruss.qurio.view.utils.BaseFragment
import com.aredruss.qurio.view.utils.formatDate
import com.aredruss.qurio.view.utils.getClearDate
import com.aredruss.qurio.view.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class NoteFragment : BaseFragment(R.layout.fragment_note) {

    private val binding: FragmentNoteBinding by viewBinding(FragmentNoteBinding::bind)
    private val editorViewModel: EditorViewModel by viewModel { parametersOf(args.note) }
    private val args: NoteFragmentArgs by navArgs()
    private val fragmentInput: Pair<String, String>
        get() {
            return getInput()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        args.note?.let { showNote(it) }

        activity?.onBackPressedDispatcher?.addCallback { findNavController().popBackStack() }

        observeState()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_note, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                editorViewModel.deleteNote()
                findNavController().popBackStack()
            }
            R.id.action_share -> openNoteShareDialog()
            else -> saveAndExit()
        }
        return true
    }

    private fun observeState() {
        editorViewModel.noteState.observe(viewLifecycleOwner) { state ->
            if (state.isNewNote) {
                setSaveMode()
            } else {
                setEditMode()
            }
            state.currentNote?.let {
                showNote(it)
            }
        }
    }

    private fun setSaveMode() = with(binding) {
        (requireActivity() as MainActivity).setToolbarTitle("Create")
        dateTv.text = Calendar.getInstance().time.formatDate()
    }

    private fun setEditMode() {
        (requireActivity() as MainActivity).setToolbarTitle("Edit")
    }

    private fun showNote(note: Note) = with(binding) {
        titleEt.setText(note.title)
        bodyEt.setText(note.text)
        dateTv.text = note.date.formatDate()
    }

    private fun getInput(): Pair<String, String> = with(binding) {
        val title = (titleEt.text.toString()).ifEmpty { "Untitled" }
        val body = bodyEt.text.toString()
        return@with title to body
    }

    private fun writeNote() {
        if (args.note == null) {
            createFromInput()
        } else {
            updateFromInput()
        }
    }

    private fun saveAndExit() {
        writeNote()
        findNavController().popBackStack()
    }

    private fun createFromInput() {
        editorViewModel.createNote(fragmentInput.first, fragmentInput.second)
    }

    private fun updateFromInput() {
        editorViewModel.updateNote(fragmentInput.first, fragmentInput.second)
    }

    private fun openNoteShareDialog() {
        writeNote()
        val note = LiteNote(
            fragmentInput.first,
            fragmentInput.second,
            Calendar.getInstance().getClearDate().formatDate() ?: ""
        )
        openShareDialog(note)
    }
}
