package com.tanmay.sneakerapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "cart")
@Parcelize
data class SneakerItem(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val brand: String,
    val media: Media,
    val name: String,
    val releaseDate: String,
    val retailPrice: Int,
    val title: String,
    var isAddedToCart: Boolean = false
): Parcelable

class Converters {
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()


    @TypeConverter
    fun fromMedia(media: Media?): String? {
        if (media == null) return null
        val type = Types.newParameterizedType(Media::class.java, Media::class.java)
        val adapter: JsonAdapter<Media> = moshi.adapter(type)
        return adapter.toJson(media)
    }

    @TypeConverter
    fun toMedia(json: String?): Media? {
        if (json == null) return null
        val type = Types.newParameterizedType(Media::class.java, Media::class.java)
        val adapter: JsonAdapter<Media> = moshi.adapter(type)
        return adapter.fromJson(json)
    }
}