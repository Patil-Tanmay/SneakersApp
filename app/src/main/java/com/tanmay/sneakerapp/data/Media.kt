package com.tanmay.sneakerapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Media(
    val imageUrl: String,
    val thumbUrl: String
): Parcelable