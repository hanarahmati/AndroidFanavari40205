package co.fanavari.androidfanavari40205.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData

class ConnectionLiveData(context: Context):LiveData<Boolean> () {

    private lateinit var networkCallback: NetworkCallback
    private val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val ValidNetwork: MutableSet<Network> = HashSet()

    private fun checkValidNetwork(){
        postValue(ValidNetwork.size > 0)
    }

    override fun onActive() {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()
        cm.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        cm.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d("CManger","onAvailable: $network")
            val networkCapabilities = cm.getNetworkCapabilities(network)
            val hasNetworkCapability = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)
            Log.d("CManager","onAvailable: $hasNetworkCapability")
            if (hasNetworkCapability == true){
                ValidNetwork.add(network)
                checkValidNetwork()
            }
        }

        override fun onLost(network: Network) {
            ValidNetwork.remove(network)
            checkValidNetwork()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)

            if(networkCapabilities.hasCapability(NET_CAPABILITY_INTERNET))
            {
                ValidNetwork.add(network)
                checkValidNetwork()
            }else{
                ValidNetwork.remove(network)
                checkValidNetwork()
            }
        }
    }

}