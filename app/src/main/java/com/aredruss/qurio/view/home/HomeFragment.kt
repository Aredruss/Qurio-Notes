package com.aredruss.qurio.view.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aredruss.qurio.R
import com.aredruss.qurio.databinding.FragmentHomeBinding
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.view.MainActivity
import com.aredruss.qurio.view.utils.safeNavigate
import com.aredruss.qurio.view.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val homeViewModel: HomeViewModel by viewModel()
    lateinit var adapter: NoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NoteAdapter(this::navigateToNote, this::deleteNote)

        activity?.actionBar?.setDisplayShowHomeEnabled(false)
        setHasOptionsMenu(true)

        binding.apply {

            (requireActivity() as MainActivity).setToolbarTitle(getString(R.string.app_name))

            notesRv.adapter = adapter
            notesRv.layoutManager = LinearLayoutManager(requireContext())

            createFab.setOnClickListener {
                navigateToNote(null)
            }
        }

        homeViewModel.notesListStateLD.observe(viewLifecycleOwner) { state ->
            state?.loading?.consume()?.let {

            }

            state?.error?.consume()?.let {

            }

            if (state.isEmpty) {

            }

            adapter.submitList(state.userNotes)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                findNavController().popBackStack()
            }
            R.id.action_settings -> {}
            else -> findNavController().popBackStack()
        }
        return true
    }

    private fun navigateToNote(note: Note?) {
        findNavController().safeNavigate(HomeFragmentDirections.homeToNote(note))
    }

    private fun deleteNote(note: Note) = homeViewModel.deleteNote(note)
}
