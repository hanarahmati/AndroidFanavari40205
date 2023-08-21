package co.fanavari.androidfanavari40205.ui.task

import androidx.lifecycle.*
import co.fanavari.androidfanavari40205.ADD_TASK_RESULT_OK
import co.fanavari.androidfanavari40205.EDIT_TASK_RESULT_OK
import co.fanavari.androidfanavari40205.data.task.Task
import co.fanavari.androidfanavari40205.data.task.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    state: SavedStateHandle
) : ViewModel() {

    //val tasks = taskDao.getTask().asLiveData()

    //    val searchQuery = MutableStateFlow("")
    val searchQuery = state.getLiveData("searchQuery", "")

    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted = MutableStateFlow(false)

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    /*private val tasksFlow = searchQuery.flatMapLatest {
        taskDao.getTasks(it)
    }*/


    @OptIn(ExperimentalCoroutinesApi::class)
    private val tasksFlow = combine(
        searchQuery.asFlow()
        , sortOrder, hideCompleted
    ) { query, sortOrder, hideCompleted ->
        Triple(query, sortOrder, hideCompleted)
    }.flatMapLatest { (query, sortOrder, hideCompleted) ->
        taskDao.getTasks(query, sortOrder, hideCompleted)
    }

    val tasks = tasksFlow.asLiveData()

    fun onTaskCheckedChanged(task: Task, isChecked: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.update(task.copy(completed = isChecked))
    }

    fun onTaskSwiped(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.delete(task)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun onUndoDeleteClick(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.insert(task)
    }

    fun onAddNewTaskClick() = viewModelScope.launch(Dispatchers.IO) {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onTaskSelected(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun onAddEditResult(result : Int){
        when(result){
            ADD_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task Added")
            EDIT_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task Updated")
        }
    }
    private fun showTaskSavedConfirmationMessage(text: String) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMessage(text))
    }

    fun onDeleteAllCompletedClick()  = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToDeleteAllCompletedScreen)
    }


    sealed class TasksEvent {
        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
        data class ShowTaskSavedConfirmationMessage(val msg: String) : TasksEvent()
        object NavigateToDeleteAllCompletedScreen: TasksEvent()
    }

}

enum class SortOrder { BY_NAME, BY_DATE }