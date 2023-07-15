package com.example.cimbsenews.adapter
import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.cimbsenews.ui.DetailsPostActivity
import com.example.cimbsenews.R
import com.example.cimbsenews.databinding.ItemRowBinding
import com.example.cimbsenews.response.PostListResponseItem
import java.util.*
import kotlin.collections.ArrayList

class PostAdapter :
    ListAdapter<PostListResponseItem, PostAdapter.ViewHolder>(PostDiffCallback()), Filterable {

    private var postList: List<PostListResponseItem> = ArrayList()
    private var filteredPostList: List<PostListResponseItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredPostList[position])
    }

    override fun getItemCount(): Int = filteredPostList.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(postList: List<PostListResponseItem>) {
        this.postList = postList
        this.filteredPostList = postList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNullOrEmpty()) {
                    filterResults.values = postList
                } else {
                    val searchQuery = constraint.toString().lowercase(Locale.getDefault())
                    val filteredList = postList.filter { item ->
                        item.item_name.lowercase(Locale.getDefault()).contains(searchQuery)
                    }
                    filterResults.values = filteredList
                }
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredPostList = results.values as List<PostListResponseItem>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostListResponseItem) {
            binding.apply {
                postName.text = item.item_name
                AddedDateTextView.text = item.date
                val postImageURL = item.image
                imgPost.load(postImageURL) {
                    crossfade(true)
                    placeholder(R.drawable.post_placeholder)
                    scale(Scale.FILL)
                }
                updateDateTextView.text = item.county
                AuthorNameTextView.text = item.author

                itemView.setOnClickListener {
                    val context = itemView.context
                    val intent = Intent(context, DetailsPostActivity::class.java)
                    intent.putExtra(DetailsPostActivity.EXTRA_URL, item.link)
                    intent.putExtra(DetailsPostActivity.EXTRA_POST_NAME, item.item_name)
                    context.startActivity(intent)
                }
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<PostListResponseItem>() {
    override fun areItemsTheSame(
        oldItem: PostListResponseItem,
        newItem: PostListResponseItem
    ): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(
        oldItem: PostListResponseItem,
        newItem: PostListResponseItem
    ): Boolean {
        return oldItem == newItem
    }
}
