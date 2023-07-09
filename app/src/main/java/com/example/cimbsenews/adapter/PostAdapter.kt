package com.example.cimbsenews.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.cimbsenews.ui.DetailsPostActivity
import com.example.cimbsenews.R
import com.example.cimbsenews.databinding.ItemRowBinding
import com.example.cimbsenews.response.PostListResponseItem

class PostAdapter(private val context: Context) :
    ListAdapter<PostListResponseItem, PostAdapter.ViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
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
                updateDateTextView.text = item.update_date.toString()
                AuthorNameTextView.text = item.author

                itemView.setOnClickListener {
                    val context = itemView.context
                    val intent = Intent(context, DetailsPostActivity::class.java)
                    intent.putExtra(DetailsPostActivity.EXTRA_URL, item.link) // Replace 'url' with the actual URL property in your item
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
