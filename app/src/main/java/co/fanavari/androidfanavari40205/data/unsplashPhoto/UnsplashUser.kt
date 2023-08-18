package co.fanavari.androidfanavari40205.data.unsplashPhoto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UnsplashUser(
    val name: String,
    val username: String
): Parcelable {

}
