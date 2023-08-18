package co.fanavari.androidfanavari40205.ui.gallery

import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import co.fanavari.androidfanavari40205.R
import co.fanavari.androidfanavari40205.databinding.FragmentGalleryBinding
import co.fanavari.androidfanavari40205.utils.ConnectivityManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<GalleryViewModel>()

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectivityManager.registerConnectionObserver(this)

        _binding = FragmentGalleryBinding.bind(view)
        val adapter = UnsplashPhotoAdapter()

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
                .withLoadStateHeaderAndFooter(
                    header = UnsplashPhotoLoadStateAdapter { adapter.retry() },
                    footer = UnsplashPhotoLoadStateAdapter { adapter.retry() }
                )

            buttonRetry.setOnClickListener {
                adapter.retry()
            }
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error
            }
        }

        viewModel.photos.observe(viewLifecycleOwner)
        {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        connectivityManager.isNetWorkAvailable.observe(viewLifecycleOwner) {
            if (!it)
                Snackbar.make(binding.root, "no internet", Snackbar.LENGTH_INDEFINITE)
                    .setAction("settings"){
                        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                    }
                    .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connectivityManager.unregisterConnectionObserver(this)
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        connectivityManager.registerConnectionObserver(this)
    }
}