package com.example.cimbsenews.response

data class PostListResponseItem(
    val _id: String,
    val author: String,
    val image: String,
    val item_name: String,
    val link: String,
    val publish_date: Int,
    val update_date: Int
)