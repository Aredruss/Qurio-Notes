package com.aredruss.qurio.view.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aredruss.qurio.R
import com.aredruss.qurio.databinding.FragmentHomeBinding
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.view.MainActivity
import com.aredruss.qurio.view.utils.BaseFragment
import com.aredruss.qurio.view.utils.safeNavigate
import com.aredruss.qurio.view.utils.setSlideTransitions
import com.aredruss.qurio.view.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment(R.layout.fragment_home) {

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
            notesRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    createFab.animate().alpha(
                        if (newState != RecyclerView.SCROLL_STATE_DRAGGING) 1.0f else 0f
                    )
                }
            })
            createFab.setOnClickListener {
                navigateToNote(null)
            }
        }
        observeState()
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

    private fun observeState() {
        homeViewModel.notesListStateLD.observe(viewLifecycleOwner) { state ->
            state?.loading?.consume()?.let {

            }

            state?.error?.consume()?.let {

            }

            if (state.isEmpty) {

            }

            adapter.submit(state.userNotes)
        }
    }
}
