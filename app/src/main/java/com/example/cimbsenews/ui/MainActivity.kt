package com.example.cimbsenews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cimbsenews.R
import com.example.cimbsenews.adapter.PostAdapter
import com.example.cimbsenews.api.ApiClient
import com.example.cimbsenews.api.ApiService
import com.example.cimbsenews.databinding.ActivityMainBinding
import com.example.cimbsenews.response.PostListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var postAdapter: PostAdapter // Declare the adapter variable

    private val api: ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the adapter with the context
        postAdapter = PostAdapter(this)

        binding.apply {
            prgBarPost.visibility = View.VISIBLE

            val callPostApi = api.getPosts()
            callPostApi.enqueue(object : Callback<PostListResponse> {
                override fun onResponse(
                    call: Call<PostListResponse>,
                    response: Response<PostListResponse>
                ) {
                    prgBarPost.visibility = View.GONE
                    when (response.code()) {
                        in 200..299 -> {
                            Log.d("Response Code", " success messages : ${response.code()}")
                            response.body()?.let { postListResponse ->
                                if (postListResponse.isNotEmpty()) {
                                    postAdapter.submitList(postListResponse)
                                    //Recycler
                                    rlPosts.apply {
                                        layoutManager = LinearLayoutManager(this@MainActivity)
                                        adapter = postAdapter
                                    }
                                }
                            }
                        }
                        in 300..399 -> {
                            Log.d("Response Code", " Redirection messages : ${response.code()}")
                        }
                        in 400..499 -> {
                            Log.d("Response Code", " Client error responses : ${response.code()}")
                        }
                        in 500..599 -> {
                            Log.d("Response Code", " Server error responses : ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<PostListResponse>, t: Throwable) {
                    prgBarPost.visibility = View.GONE
                    Log.e("onFailure", "Err : ${t.message}")
                }
            })
        }
    }
}
