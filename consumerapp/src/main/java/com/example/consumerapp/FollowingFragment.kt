package com.example.consumerapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowingFragment : Fragment() {

    private val list = ArrayList<User>()

    companion object {
        private val EXTRA_NAME = "extra_name"

        fun newInstance(username: String):FollowingFragment{
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_NAME,username)
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycleviews = view.findViewById<RecyclerView>(R.id.rv_users)
        val progressbars = view.findViewById<ProgressBar>(R.id.progressBar)
        val username = arguments?.getString(FollowingFragment.EXTRA_NAME)
        if (username != null) {
            getDataUser(username,progressbars,recycleviews)
        }
    }

    private fun getDataUser(Username: String = "sidiq", pb: ProgressBar, rv:RecyclerView) {
        pb.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$Username/following"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                pb.visibility = View.INVISIBLE
                val result = responseBody?.let { String(it) }
                //Log.d(MainActivity.TAG, result)
                try {
                    val responseObject = JSONArray(result)
                    for(i in 0 until responseObject.length()){
                        val getUser = responseObject.getJSONObject(i)
                        val usernames = getUser.getString("login")
                        val photos = getUser.getString("avatar_url")
                        val ids = getUser.getInt("id")
                        val Id = ids.toString()

                        val user = User(
                                username = usernames,
                                avatar = photos,
                                id = Id)
                        list.add(user)
                        rv.layoutManager = LinearLayoutManager(activity)
                        val listUserAdapter = userAdapter(list)
                        rv.adapter = listUserAdapter
                    }
                } catch (e: Exception) {
                    //Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                pb.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                //Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }
}