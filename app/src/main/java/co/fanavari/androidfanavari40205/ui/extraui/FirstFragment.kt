package co.fanavari.androidfanavari40205.ui.extraui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import co.fanavari.androidfanavari40205.databinding.FragmentFirstBinding


class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(layoutInflater,
            container, false)

        val view = binding.root

        binding.textViewFragment1.setOnClickListener {
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(21)
            Navigation.findNavController(view).navigate(action)
        }
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_first, container, false)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}