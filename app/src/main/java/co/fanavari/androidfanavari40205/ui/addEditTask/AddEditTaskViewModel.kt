package co.fanavari.androidfanavari40205.ui.addEditTask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.fanavari.androidfanavari40205.data.task.Task
import co.fanavari.androidfanavari40205.data.task.TaskDao
import co.fanavari.androidfanavari40205.ui.navigation.ADD_TASK_RESULT_OK
import co.fanavari.androidfanavari40205.ui.navigation.EDIT_TASK_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val state: SavedStateHandle
): ViewModel() {

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()

    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName")  ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskImportance = state.get<Boolean>("taskImportance")  ?: task?.important ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }

    private fun createTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.insert(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.update(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }
    private fun showInvalidInputMessage(text: String) = viewModelScope.launch(Dispatchers.IO) {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(text))
    }

    fun onSaveClick() {
        if (taskName.isBlank()){
            showInvalidInputMessage("Name cannot be empty")
            return
        }


        if (task != null){
            val taskUpdate = task.copy(name = taskName, important = taskImportance)
            updateTask(taskUpdate)
        } else {
            val newTask = Task(name = taskName, important = taskImportance)
            createTask(newTask)
        }
    }

    sealed class AddEditTaskEvent{
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
    }
}
