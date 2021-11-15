package com.example.githubuser

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        var username: String? = null,
        var name: String? = null,
        var location: String? = null,
        var repository: String? = null,
        var company: String? = null,
        var followers: String? = null,
        var following: String? = null,
        var avatar: String? = null,
        var id: String? = null,
        var favorite: String? = null
): Parcelable
