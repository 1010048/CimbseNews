package com.example.cimbsenews.response

data class PostListResponseItem(
    val _id: String,
    val author: String,
    val county: String,
    val image: String,
    val item_name: String,
    val link: String,
    val date: String,
    val publish_date: Int,
    val update_date: Int
)