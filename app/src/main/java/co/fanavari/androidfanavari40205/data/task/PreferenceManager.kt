package co.fanavari.androidfanavari40205.data.task

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

enum class SortOrder { BY_NAME, BY_DATE}

data class FilterPreferences(val sortOrder: SortOrder, val hideCompleted: Boolean)

private const val TAG = "PreferencesManager"

@Singleton
class PreferencesManager
@Inject constructor(@ApplicationContext context: Context) {

    private val Context.dataStore:
            DataStore<Preferences>
    by preferencesDataStore(name = "user_pref")

    private val mContext = context

    val preferencesFlow = context.dataStore.data
        .catch { exception ->
            if(exception is IOException){
                Log.e(TAG, "Error reading prefs ", exception )
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            val sortOrder = SortOrder.valueOf(
                prefs[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name
            )
            val hideCompleted = prefs[PreferencesKeys.HIDE_COMPLETED] ?: false
            FilterPreferences(sortOrder, hideCompleted)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder){
        mContext.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateHideCompleted(hideCompleted: Boolean){
        mContext.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

    private object PreferencesKeys{
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val HIDE_COMPLETED = booleanPreferencesKey("hide_completed")
    }

}