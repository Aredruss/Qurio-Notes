package com.aredruss.qurio.view.home

import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aredruss.qurio.R
import com.aredruss.qurio.databinding.ItemDateBinding
import com.aredruss.qurio.databinding.ItemNoteBinding
import com.aredruss.qurio.model.LiteNote
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.model.toLiteNote
import com.aredruss.qurio.view.utils.formatDate
import com.aredruss.qurio.view.utils.getStringArgText
import com.aredruss.qurio.view.utils.viewBinding
import java.util.*

class NoteAdapter(
    private val dateAction: (Date) -> Unit,
    private val navigateClickAction: (Note) -> Unit,
    private val deleteClickAction: (Note) -> Unit,
    private val exportClickAction: (LiteNote) -> Unit
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
            1 -> NoteVh(
                binding = parent.viewBinding(ItemNoteBinding::inflate),
                navigateClickAction = navigateClickAction,
                deleteClickAction = deleteClickAction,
                exportClickAction = exportClickAction
            )
            else -> DateVh(
                binding = parent.viewBinding(ItemDateBinding::inflate),
                dateAction = dateAction
            )
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
    private val navigateClickAction: (Note) -> Unit,
    private val deleteClickAction: (Note) -> Unit,
    private val exportClickAction: (LiteNote) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(note: Note) = with(binding) {
        titleTv.text = note.title
        bodyTv.isVisible = note.text.isNotEmpty()
        bodyTv.text = note.text
        root.setOnClickListener {
            navigateClickAction(note)
        }
        root.setOnLongClickListener {
            val popupMenu = PopupMenu(it.context, it)
            popupMenu.menuInflater.inflate(R.menu.menu_note, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { itemView ->
                when (itemView.itemId) {
                    R.id.action_delete -> deleteClickAction(note)
                    R.id.action_share -> exportClickAction(note.toLiteNote())
                }
                true
            }
            popupMenu.show()
            true
        }
    }
}

data class DateVh(
    private val binding: ItemDateBinding,
    private val dateAction: (Date) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(date: Date) = with(binding) {
        dateTv.text = getStringArgText(R.string.note_date, date.formatDate() ?: "")
        root.setOnClickListener { dateAction(date) }
    }
}