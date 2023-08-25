package co.fanavari.androidfanavari40205.ui.addEditTask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import co.fanavari.androidfanavari40205.R
import co.fanavari.androidfanavari40205.databinding.FragmentAddEditTaskBinding
import co.fanavari.androidfanavari40205.utils.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditTaskBinding.bind(view)

        binding.apply {
            editTextTaskName.setText(viewModel.taskName)
            checkBoxImportant.isChecked = viewModel.taskImportance
            checkBoxImportant.jumpDrawablesToCurrentState()
            textViewDateCreated.isVisible = viewModel.task != null
            textViewDateCreated.text = "created : ${viewModel.task?.created}"

        }

        binding.editTextTaskName.addTextChangedListener {
            viewModel.taskName = it.toString()
        }

        binding.checkBoxImportant.
        setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.taskImportance = isChecked
        }

        binding.fabSaveTask.setOnClickListener {
            viewModel.onSaveClick()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.addEditTaskEvent.collect{ event ->
                    when(event){
                        is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                            binding.editTextTaskName.clearFocus()
                            requireActivity().supportFragmentManager.setFragmentResult(
                                "add_edit_req",
                                bundleOf("add_edit_result" to event.result)
                            )
                            findNavController(this@AddEditTaskFragment).popBackStack()
                        }
                        is AddEditTaskViewModel
                        .AddEditTaskEvent.ShowInvalidInputMessage -> {
                           Snackbar
                               .make(requireView(),event.msg, Snackbar.LENGTH_LONG)
                               .show()
                        }
                    }.exhaustive

                }
            }

        }

    }
}