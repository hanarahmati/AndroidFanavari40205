package co.fanavari.androidfanavari40205.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import co.fanavari.androidfanavari40205.databinding.ItemUnsplashBinding
import co.fanavari.androidfanavari40205.data.UnsplashPhoto

class UnsplashPhotoAdapter: PagingDataAdapter<UnsplashPhoto,
        PhotoViewHolder>(PHOTO_COMPARTOR) {
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemUnsplashBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotoViewHolder(binding)
    }

    companion object {
        private val PHOTO_COMPARTOR = object : DiffUtil.ItemCallback<UnsplashPhoto>(){
            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: UnsplashPhoto,
                newItem: UnsplashPhoto
            ): Boolean =
                oldItem == newItem


        }
    }
}