package co.fanavari.androidfanavari40205.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.widget.SearchView

fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(this, msg , duration).show()

}

inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit){
    this.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }

    })
}