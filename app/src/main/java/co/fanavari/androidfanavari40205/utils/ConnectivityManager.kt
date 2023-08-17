package co.fanavari.androidfanavari40205.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import co.fanavari.androidfanavari40205.MyApplication
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityManager @Inject constructor(
    application: MyApplication
) {
    private val connectionLiveData = ConnectionLiveData(application)

    val isNetWorkAvailable = MutableLiveData(true)

    fun registerConnectionObserver(lifecycleOwner: LifecycleOwner){
        connectionLiveData.observe(lifecycleOwner){ isConnected ->
            isConnected?.let {
                isNetWorkAvailable.value = it
            }
        }
    }
    fun unregisterConnectionObserver(lifecycleOwner: LifecycleOwner){
        connectionLiveData.removeObservers(lifecycleOwner)
    }
}