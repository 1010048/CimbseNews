package com.example.cimbsenews.api

import com.example.cimbsenews.response.PostListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/endpoint")
    fun getPosts(@Query("page") page : Int) : Call<PostListResponse>
}