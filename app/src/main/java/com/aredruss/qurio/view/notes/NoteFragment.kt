package com.aredruss.qurio.view.notes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.aredruss.qurio.R
import com.aredruss.qurio.databinding.FragmentNoteBinding
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.view.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NoteFragment : Fragment(R.layout.fragment_note) {

    private val binding: FragmentNoteBinding by viewBinding(FragmentNoteBinding::bind)
    private val editorViewModel: EditorViewModel by viewModel { parametersOf(args.note) }
    private val args: NoteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()

        if (args.note == null) {
            binding.apply {
                saveBtn.setOnClickListener {
                    val title = titleEt.text.toString()
                    val body = bodyEt.text.toString()
                    editorViewModel.createNote(title, body)
                }
            }
        }
    }

    private fun showNote(note: Note) = with(binding) {
        titleEt.setText(note.text)
        bodyEt.setText(note.text)
    }

    private fun observeState() {
        editorViewModel.noteState.observe(viewLifecycleOwner) { state ->
            state.currentNote?.let { note ->
                binding.saveBtn.setOnClickListener {
                    editorViewModel.updateNote(note.name, note.text)
                }
                showNote(note)
            }
        }
    }
}
