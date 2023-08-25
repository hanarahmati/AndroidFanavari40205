package co.fanavari.androidfanavari40205.ui.task

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.fanavari.androidfanavari40205.R
import co.fanavari.androidfanavari40205.data.task.SortOrder
import co.fanavari.androidfanavari40205.data.task.Task
import co.fanavari.androidfanavari40205.databinding.FragmentTaskBinding
import co.fanavari.androidfanavari40205.utils.exhaustive
import co.fanavari.androidfanavari40205.utils.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_task), TaskAdapter.OnItemClickListener {

    private val viewModel: TaskViewModel by viewModels()
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTaskBinding.bind(view)

        val taskAdapter = TaskAdapter(this)

        binding.apply {
            recyclerViewTasks.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)

            }
        }

        viewModel.tasks.observe(viewLifecycleOwner){
            taskAdapter.submitList(it)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){

                viewModel.taskEvent.collect{event ->

                    when(event){
                        is TasksEvent.ShowUndoDeleteTaskMessage -> {
                            Snackbar.make(requireView(), "Task deleted",Snackbar.LENGTH_LONG)
                                .setAction("UNDO"){
                                    viewModel.onUndoDeleteItem(event.task)
                                }
                                .show()
                        }

                        is TasksEvent.NavigateToAddTaskScreen -> {
                            val action = TaskFragmentDirections
                                .actionTaskFragmentToAddEditTaskFragment(null, "New Task")
                            findNavController().navigate(action)
                        }
                        is TasksEvent.NavigateToEditTaskScreen -> {
                            val action = TaskFragmentDirections.actionTaskFragmentToAddEditTaskFragment(event.task, "Edit Task")
                            findNavController().navigate(action)
                        }

                        is TasksEvent.ShowTaskSavedConfirmationMessage -> {
                            Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                        }

                        is TasksEvent.NavigateToDeleteAllCompletedScreen -> {
                            val action = TaskFragmentDirections.actionGlobalDeleteAllCompletedDialogFragment()
                            findNavController().navigate(action)

                        }
                    }.exhaustive

                }
            }

        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_fragment_tasks, menu)

                val searchItem = menu.findItem(R.id.action_search)
                searchView = searchItem.actionView as SearchView

                val pendingQuery = viewModel.searchQuery.value

                if(!pendingQuery.isNullOrEmpty()){
                    searchItem.expandActionView()
                    searchView.setQuery(pendingQuery,false)
                }

                searchView.onQueryTextChanged {
                    viewModel.searchQuery.value = it
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    menu.findItem(R.id.action_hide_completed_tasks).isChecked =
                        viewModel.preferencesFlow.first().hideCompleted
                }


            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_sort_by_name -> {
                        viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                        true
                    }

                    R.id.action_sort_by_date_created -> {
                        viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                        true
                    }

                    R.id.action_hide_completed_tasks -> {

                        menuItem.isChecked = !menuItem.isChecked
                        viewModel.onHideCompletedClicked(menuItem.isChecked)
                        true
                    }

                    R.id.action_delete_all_completed_tasks -> {

                        viewModel.onDeleteAllCompletedClick()
                        true
                    }

                    else -> false
                }
            }


        },viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.fabAddTask.setOnClickListener {
            viewModel.onAddNewTask()
        }

        requireActivity().supportFragmentManager
            .setFragmentResultListener(
                "add_edit_req", viewLifecycleOwner)
            { _ , bundle ->
                val result = bundle.getInt("add_edit_result")
                viewModel.onAddEditResult(result)
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task, isCheck: Boolean) {
        viewModel.onTaskCheckChanged(task, isCheck)
    }
}

/*
enum class SortOrder{
    BY_NAME, BY_DATE
}*/
