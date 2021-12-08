package com.aredruss.qurio.view.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aredruss.qurio.databinding.ItemNoteBinding
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.view.utils.formatDate
import com.aredruss.qurio.view.utils.viewBinding

class NoteAdapter(
    private val clickAction: (Note) -> Unit,
    private val swipeAction: (Note) -> Unit
) : ListAdapter<Note, NoteVh>(Differ()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteVh {
        return NoteVh(parent.viewBinding(ItemNoteBinding::inflate), clickAction, swipeAction)
    }

    override fun onBindViewHolder(holder: NoteVh, position: Int) {
        holder.bind(currentList[position])
    }
}

class Differ : DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Note, newItem: Note) =
        oldItem.id == newItem.id
}

data class NoteVh(
    private val binding: ItemNoteBinding,
    private val clickAction: (Note) -> Unit,
    private val swipeAction: (Note) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(note: Note) {
        binding.titleTv.text = note.name
        binding.bodyTv.text = note.text
        binding.dateTv.text = note.date.formatDate()

        binding.root.setOnClickListener {
            clickAction(note)
        }

        binding.root.setOnLongClickListener {
            swipeAction(note)
            true
        }
    }
}