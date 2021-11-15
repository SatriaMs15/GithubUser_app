package com.example.githubuser

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.githubuser.database.DatabaseUser.UserColumns.Companion.AVATAR
import com.example.githubuser.database.DatabaseUser.UserColumns.Companion.COMPANY
import com.example.githubuser.database.DatabaseUser.UserColumns.Companion.CONTENT_URI
import com.example.githubuser.database.DatabaseUser.UserColumns.Companion.FAVORITE
import com.example.githubuser.database.DatabaseUser.UserColumns.Companion.USERNAME
import com.example.githubuser.database.DatabaseUser.UserColumns.Companion._ID
import com.example.githubuser.database.UserHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.squareup.picasso.Picasso
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailFragment : Fragment() {

    private lateinit var getHelper: UserHelper
    private var statusFavorite = false
    private lateinit var imageAvatar: String
    private lateinit var favorite: String
    private lateinit var uriWithId: Uri

    companion object {

        private val TAG = DetailFragment::class.java.simpleName

        private val EXTRA_NAME = "extra_name"
        private val EXTRA_ID = "extra_id"

        fun newInstance(username: String):DetailFragment{
            val fragment = DetailFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_NAME,username)
            bundle.putString(EXTRA_ID,username)
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressbars = view.findViewById<ProgressBar>(R.id.progressBar)
        val username = arguments?.getString(EXTRA_NAME)
        val tvName: TextView = view.findViewById(R.id.Name)
        val tvusername: TextView = view.findViewById(R.id.username)
        val tvid: TextView = view.findViewById(R.id.txt_id)
        val tvCompany: TextView = view.findViewById(R.id.txt_company)
        val tvLocation: TextView = view.findViewById(R.id.txt_Location)
        val tvFollowers: TextView = view.findViewById(R.id.txt_followers)
        val tvFollowing: TextView = view.findViewById(R.id.txt_following)
        val tvRepository: TextView = view.findViewById(R.id.txt_repository)
        val tvPhoto: ImageView = view.findViewById(R.id.img_photo)

        val fab:FloatingActionButton = view.findViewById(R.id.fab_add)

        //getHelper = context?.let { UserHelper.getInstance(it)}!!
        //getHelper.open()

        if (username != null) {
            getDataUser(username,progressbars,tvusername,tvid,tvName,tvCompany,tvLocation,tvFollowers,tvFollowing,tvRepository,tvPhoto,fab)
        }

        //var cursor = username?.let { getHelper.queryById(it) }

        fab.setOnClickListener{
            statusFavorite = !statusFavorite
            setStatusFavorite(statusFavorite)
        }

    }

    private fun getDataUser(Username: String = "sidiq",
                            pb: ProgressBar,
                            tvusername:TextView,
                            tvid:TextView,
                            tvName:TextView,
                            tvCompany:TextView,
                            tvLocation:TextView,
                            tvFollowers:TextView,
                            tvFollowing:TextView,
                            tvRepository:TextView,
                            tvPhoto:ImageView,
                            fab:FloatingActionButton) {

        pb.visibility = View.VISIBLE
        val lists = ArrayList<User>()
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = " https://api.github.com/users/$Username"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                pb.visibility = View.INVISIBLE
                val result = String(responseBody)
                //Log.d(MainActivity.TAG, result)
                try {
                    val responseObject = JSONObject(result)
                    val usernames = responseObject.getString("login")
                    val name = responseObject.getString("name")
                    val location = responseObject.getString("location")
                    val company = responseObject.getString("company")
                    val repo = responseObject.getInt("public_repos")
                    val follow = responseObject.getInt("followers")
                    val following = responseObject.getInt("following")
                    val photos = responseObject.getString("avatar_url")
                    val ids = responseObject.getInt("id")

                    nullCheking(usernames,tvusername)
                    nullCheking(name,tvName)
                    nullCheking(company,tvCompany)
                    nullCheking(location,tvLocation)
                    nullCheking(follow.toString(),tvFollowers)
                    nullCheking(following.toString(),tvFollowing)
                    nullCheking(repo.toString(),tvRepository)
                    nullCheking(ids.toString(),tvid)

                    Picasso.get().load(photos).into(tvPhoto)
                    imageAvatar = photos

                    uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + ids)
                    var cursor = context?.contentResolver?.query(uriWithId, null, null, null, null)
                    Log.d(TAG, uriWithId.toString())
                    Log.d(TAG, cursor.toString())
                    if (cursor != null) {
                        if (cursor.count > 0) {
                            var users = MappingHelper.mapCursorToObject(cursor)
                            favorite = users.favorite.toString()

                            Log.d(TAG, favorite)
                            if (favorite == "1"){
                                val checked: Int = R.drawable.ic_baseline_favorite_24
                                fab.setImageResource(checked)
                                statusFavorite = true
                                cursor.close()
                            }
                            else{
                                statusFavorite = false
                            }
                        }
                    }

                } catch (e: Exception) {
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
            }
        })

    }

    private fun nullCheking(string:String , tv:TextView){
        if (string == "null") {
            tv.text = "-"
        }
        else{
            tv.text = string
        }
    }

    private fun setStatusFavorite(statusFavorites:Boolean){
        val getName: TextView = requireView().findViewById(R.id.username)
        val dataCompany: TextView = requireView().findViewById(R.id.txt_company)
        val getid: TextView = requireView().findViewById(R.id.txt_id)
        val fav = view?.findViewById<FloatingActionButton>(R.id.fab_add)
        if (statusFavorites){
            if (fav != null) {
                val dataUsername = getName.text.toString().trim()
                val dataAvatar = imageAvatar
                val datacompany = dataCompany.text.toString().trim()
                val dataFavorite = "1"
                val dataids = getid.text.toString().trim()

                Log.d(TAG, dataUsername)

                val values = ContentValues()
                values.put(USERNAME, dataUsername)
                values.put(AVATAR, dataAvatar)
                values.put(COMPANY, datacompany)
                values.put(FAVORITE, dataFavorite)
                values.put(_ID, dataids)

                Toast.makeText(context, getString(R.string.add_favorite), Toast.LENGTH_SHORT).show()
                fav.setImageResource(R.drawable.ic_baseline_favorite_24)

                // Gets the data repository in write mode
                //getHelper.insert(values)
                context?.contentResolver?.insert(CONTENT_URI, values)
            }
        }
        else{

            if (fav != null) {
                fav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                //getHelper.deleteById(getName.text.toString().trim())
                context?.contentResolver?.delete(uriWithId, null, null)
                Toast.makeText(context, getString(R.string.delete_favorite), Toast.LENGTH_SHORT).show()
            }
        }
    }

}