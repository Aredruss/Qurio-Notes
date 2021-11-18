package com.aredruss.qurio.view.notes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.aredruss.qurio.R
import com.aredruss.qurio.databinding.FragmentNoteBinding
import com.aredruss.qurio.view.utils.viewBinding

class NoteFragment : Fragment(R.layout.fragment_note) {
    private val binding: FragmentNoteBinding by viewBinding(FragmentNoteBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
