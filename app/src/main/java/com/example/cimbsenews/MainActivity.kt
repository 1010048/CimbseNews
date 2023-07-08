package com.example.cimbsenews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cimbsenews.adapter.PostAdapter
import com.example.cimbsenews.api.ApiClient
import com.example.cimbsenews.api.ApiService
import com.example.cimbsenews.databinding.ActivityMainBinding
import com.example.cimbsenews.response.PostListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    private val postAdapter by lazy {PostAdapter()}

    private val api : ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            prgBarPost.visibility= View.VISIBLE

            val callPostApi = api.getPosts()
            callPostApi.enqueue(object : Callback<PostListResponse>{
                /**
                 * Invoked for a received HTTP response.
                 *
                 *
                 * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
                 * Call [Response.isSuccessful] to determine if the response indicates success.
                 */
                override fun onResponse(
                    call: Call<PostListResponse>,
                    response: Response<PostListResponse>
                ) {
                    prgBarPost.visibility= View.GONE
                    when(response.code()) {
                        in 200..299 -> {
                            Log.d("Response Code", " success messages : ${response.code()}")
                                response.body()?.let { postListResponse ->
                                    if (postListResponse.isNotEmpty()) {
                                        postAdapter.differ.submitList(postListResponse)
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

                /**
                 * Invoked when a network exception occurred talking to the server or when an unexpected exception
                 * occurred creating the request or processing the response.
                 */
                override fun onFailure(call: Call<PostListResponse>, t: Throwable) {
                    prgBarPost.visibility = View.GONE
                    Log.e("onFailure", "Err : ${t.message}")
                }

            })
        }
    }
}