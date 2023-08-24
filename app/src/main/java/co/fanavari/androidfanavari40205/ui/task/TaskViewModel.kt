package co.fanavari.androidfanavari40205.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import co.fanavari.androidfanavari40205.data.task.Task
import co.fanavari.androidfanavari40205.data.task.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskDao: TaskDao
): ViewModel(){


//    val tasks = taskDao.getTask().asLiveData()

    val searchQuery = MutableStateFlow("")
    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted = MutableStateFlow(false)
    /*private val tasksFlow = searchQuery.flatMapLatest {
        taskDao.getTask(it)
    }*/

    private val tasksEventChannel = Channel<TasksEvent>()
    val taskEvent = tasksEventChannel.receiveAsFlow()

    private val tasksFlow = combine(
        searchQuery,
        sortOrder,
        hideCompleted
        )
    {query, sortOrder, hideCompleted ->
        Triple(query, sortOrder, hideCompleted)
    }.flatMapLatest { (query, sortOrder, hideCompleted) ->
        taskDao.getTasks(query, sortOrder, hideCompleted)
    }

    val tasks = tasksFlow.asLiveData()

    fun onTaskCheckChanged(task: Task, isChecked: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.update(task.copy(completed = isChecked))
    }

    fun onTaskSwiped(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.delete(task)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun onUndoDeleteItem(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.insert(task)
    }
}

sealed class TasksEvent {
    data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
}