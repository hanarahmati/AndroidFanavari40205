package co.fanavari.androidfanavari40205.ui.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import co.fanavari.androidfanavari40205.data.task.Task
import co.fanavari.androidfanavari40205.data.task.TaskDao
import co.fanavari.androidfanavari40205.ui.navigation.ADD_TASK_RESULT_OK
import co.fanavari.androidfanavari40205.ui.navigation.EDIT_TASK_RESULT_OK
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
    private val taskDao: TaskDao,
    private val state: SavedStateHandle
) : ViewModel() {


//    val tasks = taskDao.getTask().asLiveData()

//    val searchQuery = MutableStateFlow("")
    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted = MutableStateFlow(false)

    val searchQuery = state.getLiveData("searchQuery", "")
    /*private val tasksFlow = searchQuery.flatMapLatest {
        taskDao.getTask(it)
    }*/

    private val tasksEventChannel = Channel<TasksEvent>()
    val taskEvent = tasksEventChannel.receiveAsFlow()

    private val tasksFlow = combine(
        searchQuery.asFlow(),
        sortOrder,
        hideCompleted
    )
    { query, sortOrder, hideCompleted ->
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

    fun onAddNewTask() = viewModelScope.launch(Dispatchers.IO) {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onTaskSelected(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun onAddEditResult(result: Int) {

        when(result){
            ADD_TASK_RESULT_OK -> showTaskSaveConfirmationMessage("Task added")
            EDIT_TASK_RESULT_OK -> showTaskSaveConfirmationMessage("Task updated")
        }
    }

    fun showTaskSaveConfirmationMessage(text: String) = viewModelScope.launch(Dispatchers.IO) {
        tasksEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMessage(text))
    }

}

sealed class TasksEvent {
    data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
    object NavigateToAddTaskScreen : TasksEvent()
    data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
    data class ShowTaskSavedConfirmationMessage(val msg: String) : TasksEvent()
}