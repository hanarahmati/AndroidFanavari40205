package co.fanavari.androidfanavari40205.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import co.fanavari.androidfanavari40205.data.task.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskDao: TaskDao
): ViewModel(){


    val tasks = taskDao.getTask().asLiveData()
}