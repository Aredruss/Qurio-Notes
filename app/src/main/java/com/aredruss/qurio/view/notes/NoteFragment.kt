package com.aredruss.qurio.view.notes

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aredruss.qurio.R
import com.aredruss.qurio.databinding.FragmentNoteBinding
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.view.MainActivity
import com.aredruss.qurio.view.utils.formatDate
import com.aredruss.qurio.view.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import java.util.*

class NoteFragment : Fragment(R.layout.fragment_note) {

    private val binding: FragmentNoteBinding by viewBinding(FragmentNoteBinding::bind)
    private val editorViewModel: EditorViewModel by viewModel { parametersOf(args.note) }
    private val args: NoteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        args.note?.let {
            showNote(it)
        }

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
            R.id.action_share -> Timber.i("onOptionsItemSelected - share")
            else -> findNavController().popBackStack()
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
        saveBtn.setOnClickListener {
            val input = getInput()
            editorViewModel.createNote(input.first, input.second)
        }
    }

    private fun setEditMode() = with(binding) {
        (requireActivity() as MainActivity).setToolbarTitle("Edit")
        saveBtn.setOnClickListener {
            val input = getInput()
            editorViewModel.updateNote(input.first, input.second)
        }
    }

    private fun showNote(note: Note) = with(binding) {
        titleEt.setText(note.name)
        bodyEt.setText(note.text)
        dateTv.text = note.date.formatDate()
    }

    private fun getInput(): Pair<String, String> = with(binding) {
        val title = (titleEt.text.toString()).ifEmpty { "Untitled" }
        val body = bodyEt.text.toString()
        return@with title to body
    }
}
