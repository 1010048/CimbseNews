package com.example.cimbsenews.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.cimbsenews.R
import com.example.cimbsenews.databinding.ItemRowBinding
import com.example.cimbsenews.response.PostListResponseItem

class PostAdapter : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private lateinit var binding: ItemRowBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRowBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ViewHolder(private val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostListResponseItem) {
            binding.apply {
                postName.text = item.item_name
                AddedDateTextView.text = item.publish_date.toString()
                val postImageURL = item.image
                imgPost.load(postImageURL) {
                    crossfade(true)
                    placeholder(R.drawable.post_placeholder)
                    scale(Scale.FILL)
                }
                updateDateTextView.text = item.update_date.toString()
                AuthorNameTextView.text = item.author
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<PostListResponseItem>() {
        override fun areItemsTheSame(oldItem: PostListResponseItem, newItem: PostListResponseItem): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: PostListResponseItem, newItem: PostListResponseItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}
