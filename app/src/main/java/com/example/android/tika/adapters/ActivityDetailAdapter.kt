package com.example.android.tika.adapters

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.tika.data.models.Comment
import com.example.android.tika.data.models.Task
import com.example.android.tika.databinding.CommentLayoutBinding
import com.example.android.tika.databinding.TaskTitleItemBinding
import java.lang.IllegalArgumentException

class ActivityDetailAdapter(private val items: MutableList<Any>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TITLE = 0
        const val COMMENT = 1
    }

    init {
        notifyDataSetChanged()
    }

    class ActivityDetailDiffCallBack(private val newItems: List<Any>, private val oldItems: List<Any>)
        : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldItems[oldItemPosition]
            val new = newItems[newItemPosition]
            return old::class.simpleName == new::class.simpleName && old == new
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldItems[oldItemPosition]
            val new = newItems[newItemPosition]
            return old == new
        }
    }

    class TitleViewHolder(private val itemBinding: TaskTitleItemBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Any) {

            val resources = itemBinding.root.context.resources

            itemBinding.apply {
                titleText.text = (item as String)
                val layoutParams = titleText.layoutParams as LinearLayout.LayoutParams
                layoutParams.topMargin  = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, when (adapterPosition) {
                        0 -> 0F
                        else -> 16F
                    }, resources.displayMetrics).toInt()
                titleText.layoutParams = layoutParams
            }
        }
    }

    class CommentViewHolder(private val itemBinding: CommentLayoutBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Any) {
            val comment = item as Comment
            itemBinding.apply {
                authorText.text = comment.author.firstName
                commentText.text = comment.message
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TITLE -> TitleViewHolder(TaskTitleItemBinding.inflate(inflater, parent, false))
            COMMENT -> CommentViewHolder(CommentLayoutBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException("Unknown ViewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TITLE -> (holder as TitleViewHolder).bind(items[position])
            COMMENT -> (holder as CommentViewHolder).bind(items[position])
            else ->  throw IllegalArgumentException("Unknown ViewType ${holder.itemViewType}")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is String) TITLE else COMMENT
    }

    fun addAll(items: List<Any>) {
        val diffCallBack = ActivityDetailDiffCallBack(items, this.items)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        this.items.addAll(items)

        // calls the adapter's notify methods after diff is calculated
        diffResult.dispatchUpdatesTo(this)
    }
}