package com.example.consumerapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.database.DatabaseUser
import com.example.consumerapp.database.DatabaseUser.UserColumns.Companion.CONTENT_URI
import com.example.consumerapp.database.UserHelper
import com.example.consumerapp.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private var list = ArrayList<User>()
    private lateinit var adapter: FavoriteAdapter
    private lateinit var getHelper: UserHelper

    companion object {
        private val TAG = FavoriteActivity::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvUsers.setHasFixedSize(true)

        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        supportActionBar?.title = "Favorite"

        var cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                do {
                    val user = User(
                        username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseUser.UserColumns.USERNAME)),
                        avatar = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseUser.UserColumns.AVATAR)),
                        id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseUser.UserColumns.COMPANY))
                    )
                    list.add(user)
                    cursor.moveToNext()
                } while (!cursor.isAfterLast)
            }
        }
        binding.progressBar.visibility = View.INVISIBLE
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = FavoriteAdapter(list)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : userAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                val moveWithObjectIntent = Intent(this@FavoriteActivity, DetailPage::class.java)
                moveWithObjectIntent.putExtra(DetailPage.EXTRA_USER,data.username)
                startActivity(moveWithObjectIntent)
            }
        })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}