package com.aredruss.qurio.view.home

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aredruss.qurio.databinding.ItemDateBinding
import com.aredruss.qurio.databinding.ItemNoteBinding
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.view.utils.formatDate
import com.aredruss.qurio.view.utils.viewBinding
import timber.log.Timber
import java.util.*

class NoteAdapter(
    private val clickAction: (Note) -> Unit,
    private val swipeAction: (Note) -> Unit
) : ListAdapter<Any, RecyclerView.ViewHolder>(Differ()) {

    @OptIn(ExperimentalStdlibApi::class)
    fun submit(map: Map<Date, List<Note>>) {
        val entryList = buildList {
            map.forEach { entry ->
                add((entry.key))
                addAll(entry.value)
            }
        }
        submitList(entryList)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is Note) 1 else 0
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> NoteVh(parent.viewBinding(ItemNoteBinding::inflate), clickAction, swipeAction)
            else -> DateVh(parent.viewBinding(ItemDateBinding::inflate))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NoteVh -> holder.bind(getItem(position) as Note)
            is DateVh -> holder.bind(getItem(position) as Date)
        }
    }
}

class Differ : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Any, newItem: Any) = true
}

data class NoteVh(
    private val binding: ItemNoteBinding,
    private val clickAction: (Note) -> Unit,
    private val swipeAction: (Note) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(note: Note) = with(binding) {
        titleTv.text = note.title
        bodyTv.isVisible = note.text.isNotEmpty()
        bodyTv.text = note.text
        root.setOnClickListener {
            clickAction(note)
        }
        root.setOnLongClickListener {
            swipeAction(note)
            true
        }
    }
}

data class DateVh(private val binding: ItemDateBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(date: Date) = with(binding) {
        dateTv.text = "[${date.formatDate()}]"
    }
}