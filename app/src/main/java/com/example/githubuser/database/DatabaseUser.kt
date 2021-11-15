package com.example.githubuser.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseUser {

    const val AUTHORITY = "com.example.githubuser"
    const val SCHEME = "content"

    class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user_favorite"
            const val USERNAME = "username"
            const val _ID = "id"
            const val AVATAR = "avatar"
            const val COMPANY = "company"
            const val FAVORITE = "favorite"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}