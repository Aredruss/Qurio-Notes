package com.aredruss.qurio.view.notes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aredruss.qurio.R
import com.aredruss.qurio.databinding.FragmentHomeBinding
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.view.utils.safeNavigate
import com.aredruss.qurio.view.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val noteViewModel: NoteViewModel by viewModel()
    lateinit var adapter: NoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NoteAdapter(this::navigateToNote, this::deleteNote)

        binding.apply {
            notesRv.adapter = adapter

            createFab.setOnClickListener {
                navigateToNote(null)
            }
        }

        noteViewModel.notesStateLD.observe(viewLifecycleOwner) { state ->
            state?.loading?.consume()?.let {

            }

            state?.error?.consume()?.let {

            }

            state?.isEmpty

            adapter.submitList(state.userNotes)
        }
    }

    private fun navigateToNote(note: Note?) {
        findNavController().safeNavigate(HomeFragmentDirections.homeToNote(note))
    }

    private fun deleteNote(note: Note) {

    }
}
