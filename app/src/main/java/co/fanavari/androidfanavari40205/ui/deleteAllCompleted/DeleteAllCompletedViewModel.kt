package co.fanavari.androidfanavari40205.ui.deleteAllCompleted

import androidx.lifecycle.ViewModel
import co.fanavari.androidfanavari40205.data.task.TaskDao
import co.fanavari.androidfanavari40205.di.ApplicationScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteAllCompletedViewModel @Inject constructor(
    private val taskDao: TaskDao,
    @ApplicationScope private val applicationScope: CoroutineScope

): ViewModel() {
    fun onConfirmClick()  = applicationScope.launch {
        taskDao.deleteCompletedTasks()
    }
}