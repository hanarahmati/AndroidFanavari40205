package co.fanavari.androidfanavari40205.data.task

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import co.fanavari.androidfanavari40205.ui.task.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    /*
        @Query("SELECT * FROM TASK_TABLE")
        fun getTask(): Flow<List<Task>>*/

    /*@Query("SELECT * FROM TASK_TABLE WHERE name LIKE '%' || :searchQuery || '%' ORDER BY important DESC")
    fun getTask(searchQuery: String): Flow<List<Task>>*/


    @Query("SELECT * FROM TASK_TABLE WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :query || '%' ORDER BY important DESC, created")
    fun getTasksSortedByDate(query: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM TASK_TABLE WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :query || '%' ORDER BY name")
    fun getTasksSortedByName(query: String, hideCompleted: Boolean): Flow<List<Task>>


    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
        when (sortOrder) {
            SortOrder.BY_DATE -> getTasksSortedByDate(query, hideCompleted)
            SortOrder.BY_NAME -> getTasksSortedByName(query, hideCompleted)
        }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)
}