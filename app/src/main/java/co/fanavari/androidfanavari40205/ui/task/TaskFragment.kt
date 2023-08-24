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
import androidx.recyclerview.widget.LinearLayoutManager
import co.fanavari.androidfanavari40205.R
import co.fanavari.androidfanavari40205.databinding.FragmentTaskBinding
import co.fanavari.androidfanavari40205.utils.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_task) {

    private val viewModel: TaskViewModel by viewModels()
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTaskBinding.bind(view)

        val taskAdapter = TaskAdapter()

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

//                        viewModel.onDeleteAllCompletedClick()
                        true
                    }

                    else -> false
                }
            }


        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}

enum class SortOrder{
    BY_NAME, BY_DATE
}