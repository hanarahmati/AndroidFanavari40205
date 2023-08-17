package co.fanavari.androidfanavari40205.ui.gallery

import androidx.recyclerview.widget.RecyclerView
import co.fanavari.androidfanavari40205.databinding.ItemUnsplashBinding
import co.fanavari.androidfanavari40205.data.UnsplashPhoto
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class PhotoViewHolder(private val binding: ItemUnsplashBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: UnsplashPhoto){
            binding.apply {
                titleAnimal.text = photo.user.username

                Glide.with(itemView)
                    .load(photo.urls.regular)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(animalImageView)


            }
        }

}
