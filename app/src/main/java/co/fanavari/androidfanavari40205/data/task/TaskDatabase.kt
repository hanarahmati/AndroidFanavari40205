package co.fanavari.androidfanavari40205.data.task

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import co.fanavari.androidfanavari40205.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao


    class CallBack @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            applicationScope.launch {

                dao.insert(Task("create a routine for android development"))
                dao.insert(Task("do based on your plan"))
                dao.insert(Task("read basic kotlin", important = true))
                dao.insert(Task("learn ui for android development", completed = true))
                dao.insert(Task("R&D"))
                dao.insert(Task("work with api", completed = true))
                dao.insert(Task("work with database"))

            }
        }
    }
}