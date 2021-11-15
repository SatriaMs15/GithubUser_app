package com.example.consumerapp

import android.database.Cursor
import com.example.consumerapp.database.DatabaseUser

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<User> {
        val usersList = ArrayList<User>()
        notesCursor?.apply {
            while (moveToNext()) {
                val usernames = getString(getColumnIndexOrThrow(DatabaseUser.UserColumns.USERNAME))
                val companys = getString(getColumnIndexOrThrow(DatabaseUser.UserColumns.COMPANY))
                val avatars = getString(getColumnIndexOrThrow(DatabaseUser.UserColumns.AVATAR))
                val favorites = getString(getColumnIndexOrThrow(DatabaseUser.UserColumns.FAVORITE))
                usersList.add(User(username = usernames, company = companys, avatar =  avatars, favorite = favorites))
            }
        }
        return usersList
    }

    fun mapCursorToObject(notesCursor: Cursor?): User {
        var user = User()
        notesCursor?.apply {
            moveToFirst()
            val fav = getString(getColumnIndexOrThrow(DatabaseUser.UserColumns.FAVORITE))
            user = User(favorite = fav)
        }
        return user
    }
}