package com.example.cimbsenews.ui
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cimbsenews.R
import com.example.cimbsenews.adapter.PostAdapter
import com.example.cimbsenews.api.ApiClient
import com.example.cimbsenews.api.ApiService
import com.example.cimbsenews.databinding.ActivityMainBinding
import com.example.cimbsenews.response.PostListResponse
import com.example.cimbsenews.response.PostListResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var postAdapter: PostAdapter
    private lateinit var searchView: SearchView
    private var postList: List<PostListResponseItem> = ArrayList()

    private val api: ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        postAdapter = PostAdapter(this)
        binding.rlPosts.layoutManager = LinearLayoutManager(this)
        binding.rlPosts.adapter = postAdapter

        fetchData()
    }

    private fun fetchData() {
        binding.prgBarPost.visibility = View.VISIBLE

        val callPostApi = api.getPosts()
        callPostApi.enqueue(object : Callback<PostListResponse> {
            override fun onResponse(
                call: Call<PostListResponse>,
                response: Response<PostListResponse>
            ) {
                binding.prgBarPost.visibility = View.GONE
                when (response.code()) {
                    in 200..299 -> {
                        Log.d("Response Code", " success messages : ${response.code()}")
                        response.body()?.let { postListResponse ->
                            if (postListResponse.isNotEmpty()) {
                                postList = postListResponse
                                postAdapter.submitData(postList)
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
                binding.prgBarPost.visibility = View.GONE
                Log.e("onFailure", "Err : ${t.message}")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Search..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                postAdapter.filter.filter(newText)
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_refresh) {
            fetchData()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
