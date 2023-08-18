package co.fanavari.androidfanavari40205.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import co.fanavari.androidfanavari40205.databinding.UnsplashPhotoStateFooterBinding

class UnsplashPhotoLoadStateAdapter(private val
                                    retry: () -> Unit) :
    LoadStateAdapter<UnsplashPhotoLoadStateAdapter
    .LoadStateViewHolder>() {

    inner class LoadStateViewHolder(private val binding: UnsplashPhotoStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isInvisible = loadState is LoadState.Loading
                buttonRetry.isVisible = loadState !is LoadState.Loading
                textViewError.isVisible = loadState !is LoadState.Loading
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = UnsplashPhotoStateFooterBinding.inflate(
            LayoutInflater.from(parent.context)
        ,parent,false)
        return LoadStateViewHolder(binding)
    }
}

