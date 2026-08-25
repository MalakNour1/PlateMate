package com.example.platemate.data.connectivity

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor { //connectivity observer
    val isConnected: Flow<Boolean>
}