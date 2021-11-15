package com.example.consumerapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite(
        var username: String? = null,
        var company: String? = null,
        var avatar: String? = null,
        var statusFavorite: String? = null
): Parcelable
