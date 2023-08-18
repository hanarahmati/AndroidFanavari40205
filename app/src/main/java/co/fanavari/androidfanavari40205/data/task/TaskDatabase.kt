package co.fanavari.androidfanavari40205.data.task

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import co.fanavari.androidfanavari40205.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1,)
abstract class TaskDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class CallBack @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task("db",true ))
                dao.insert(Task("api call",true ))
                dao.insert(Task("update ui" ))
                dao.insert(Task("kotlin" ))
                dao.insert(Task("jetpack" ))
                dao.insert(Task("db gg",true ))
                dao.insert(Task("db fdvdf",true ))
                dao.insert(Task("db sdcsd",true ))
                dao.insert(Task("db csdc",true ))
            }
        }
    }
}