package co.fanavari.androidfanavari40205.ui.task

import androidx.lifecycle.ViewModel
import co.fanavari.androidfanavari40205.data.task.TaskDao
import dagger.hilt.android.scopes.ViewModelScoped


class TaskViewModel @ViewModelScoped constructor(
    private val taskDao: TaskDao
): ViewModel(){
}