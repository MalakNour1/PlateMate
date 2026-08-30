package com.example.platemate.data.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkMonitorImpl(context: Context) : NetworkMonitor {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val isConnected: Flow<Boolean> = callbackFlow {

        // Send the current connection state immediately when app starts
        val initialState = isNetworkAvailable()
        android.util.Log.d("NetworkMonitor", "Initial state: $initialState")
        trySend(initialState)

        // Listen for network changes
        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                android.util.Log.d("NetworkMonitor", "Network is available")
                trySend(true)
            }

            override fun onLost(network: Network) {
                android.util.Log.d("NetworkMonitor", "Network is lost")
                trySend(false)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                android.util.Log.d("NetworkMonitor", "Internet available: $hasInternet")
                trySend(hasInternet)
            }
        }

        // Register the callback to start listening
        connectivityManager.registerDefaultNetworkCallback(callback)

        // Clean up when the flow is closed
        awaitClose {
            android.util.Log.d("NetworkMonitor", "Closing network callback")
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    // Check the current network state
    private fun isNetworkAvailable(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
}