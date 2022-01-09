package com.aredruss.qurio.view.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aredruss.qurio.R
import com.aredruss.qurio.databinding.FragmentHomeBinding
import com.aredruss.qurio.model.LiteNote
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.view.MainActivity
import com.aredruss.qurio.view.utils.BaseFragment
import com.aredruss.qurio.view.utils.animateVisibility
import com.aredruss.qurio.view.utils.context
import com.aredruss.qurio.view.utils.getDrawable
import com.aredruss.qurio.view.utils.safeNavigate
import com.aredruss.qurio.view.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val homeViewModel: HomeViewModel by viewModel()
    lateinit var adapter: NoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NoteAdapter(
            dateAction = this::filterNotesByDate,
            navigateClickAction = this::navigateToNote,
            deleteClickAction = this::deleteNote,
            exportClickAction = this::openNoteShareDialog
        )
        activity?.actionBar?.setDisplayShowHomeEnabled(false)
        setHasOptionsMenu(true)


        binding.apply {
            (requireActivity() as MainActivity).setToolbarTitle(getString(R.string.app_name))
            notesRv.adapter = adapter
            notesRv.layoutManager = LinearLayoutManager(requireContext())
            notesRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    createFab.animateVisibility(
                        newState != RecyclerView.SCROLL_STATE_DRAGGING
                    )
                }
            })

        }
        observeState()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                findNavController().navigate(HomeFragmentDirections.homeToAbout())
            }
        }
        return true
    }

    private fun observeState() {
        homeViewModel.notesListStateLD.observe(viewLifecycleOwner) { state ->
            binding.apply {
                createFab.apply {
                    setImageDrawable(
                        binding.getDrawable(
                            if (state.isFiltered)
                                R.drawable.ic_sync else R.drawable.ic_add
                        )
                    )
                    setOnClickListener(if (state.isFiltered) { _ ->
                        refreshNotes()
                    } else { _ ->
                        navigateToNote(null)
                    })
                }

                loaderLav.animateVisibility(state.loading)
                infoV.root.animateVisibility(state.isEmpty)
                notesRv.animateVisibility(
                    !state.loading
                            && state.error == null
                            && !state.isEmpty
                )
            }
            state?.error?.consume()?.let {
                Toast.makeText(
                    binding.context(), "Error getting notes", Toast.LENGTH_SHORT
                ).show()
            }
            adapter.submit(state.userNotes)
        }
    }

    private fun openNoteShareDialog(note: LiteNote) {
        openShareDialog(note)
    }

    private fun navigateToNote(note: Note?) {
        findNavController().safeNavigate(HomeFragmentDirections.homeToNote(note))
    }

    private fun deleteNote(note: Note) = homeViewModel.deleteNote(note)

    private fun filterNotesByDate(date: Date) = homeViewModel.loadNotes(date)

    private fun refreshNotes() = homeViewModel.loadNotes()
}



