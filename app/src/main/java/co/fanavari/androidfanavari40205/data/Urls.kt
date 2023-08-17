package co.fanavari.androidfanavari40205.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Urls(
    val raw: String,
    val regular: String
): Parcelable {

}
