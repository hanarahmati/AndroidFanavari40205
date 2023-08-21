package co.fanavari.androidfanavari40205.ui.task

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.fanavari.androidfanavari40205.R
import co.fanavari.androidfanavari40205.data.task.Task
import co.fanavari.androidfanavari40205.databinding.FragmentTasksBinding
import co.fanavari.androidfanavari40205.utils.exhaustive
import co.fanavari.androidfanavari40205.utils.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks), TasksAdapter.OnItemClickListener {
    private val viewModel: TasksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTasksBinding.bind(view)

        val taskAdapter = TasksAdapter(this)

        binding.apply {
            recyclerViewTasks.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        requireActivity().supportFragmentManager.setFragmentResultListener("add_edit_req",viewLifecycleOwner) {
                _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_fragment_tasks, menu)

                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView

                searchView.onQueryTextChanged {
                    viewModel.searchQuery.value = it
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_sort_by_name -> {
                        viewModel.sortOrder.value = SortOrder.BY_NAME
                        true
                    }

                    R.id.action_sort_by_date_created -> {
                        viewModel.sortOrder.value = SortOrder.BY_DATE
                        true
                    }

                    R.id.action_hide_completed_tasks -> {

                        menuItem.isChecked = !menuItem.isChecked
                        viewModel.hideCompleted.value = menuItem.isChecked
                        true
                    }

                    R.id.action_delete_all_completed_tasks -> {

                        viewModel.onDeleteAllCompletedClick()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = taskAdapter.currentList[viewHolder.absoluteAdapterPosition]
                viewModel.onTaskSwiped(task)
            }

        }).attachToRecyclerView(binding.recyclerViewTasks)


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tasksEvent.collect { event ->
                    when (event) {
                        is TasksViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
                            Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG)
                                .setAction("UNDO") {
                                    viewModel.onUndoDeleteClick(event.task)
                                }.show()
                        }

                        is TasksViewModel.TasksEvent.NavigateToAddTaskScreen -> {
                            val action =
                                TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
                                    null,
                                    "New Task"
                                )
                            findNavController(this@TasksFragment).navigate(action)
                        }

                        is TasksViewModel.TasksEvent.NavigateToEditTaskScreen -> {
                            val action =
                                TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
                                    event.task,
                                    "Edit Task"
                                )
                            findNavController(this@TasksFragment).navigate(action)
                        }

                        is TasksViewModel.TasksEvent.ShowTaskSavedConfirmationMessage -> {
                            Snackbar.make(requireView(), event.msg , Snackbar.LENGTH_SHORT).show()
                        }
                        is TasksViewModel.TasksEvent.NavigateToDeleteAllCompletedScreen -> {
                            val action = TasksFragmentDirections.actionGlobalDeleteAllCompletedDialogFragment()
                            findNavController(this@TasksFragment).navigate(action)
                        }
                    }.exhaustive
                }

            }
        }
        binding.fabAddTask.setOnClickListener {
            viewModel.onAddNewTaskClick()
        }
    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task, isChecked)
    }


}

