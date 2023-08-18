package co.fanavari.androidfanavari40205.ui.extraui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import co.fanavari.androidfanavari40205.R
import co.fanavari.androidfanavari40205.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    private val binding get() = _binding!!

    private val arg: SecondFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_second, container, false)
        _binding = FragmentSecondBinding.inflate(
            layoutInflater,
            container, false
        )
        //val view = binding.root
        binding.textViewFragment2.text =
            "this is our  ${arg.classNo} class"

        binding.textViewFragment2.setOnClickListener {
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_secondFragment_to_firstFragment) }
        }


        return binding.root
        //return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}