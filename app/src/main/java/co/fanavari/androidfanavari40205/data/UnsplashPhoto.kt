package co.fanavari.androidfanavari40205.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UnsplashPhoto(
    val id: String,
    val description: String,
    val user: UnsplashUser,
    val urls: Urls
): Parcelable {
}