package com.example.senimoapplication.server

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketManager {
    private lateinit var mSocket: Socket

    init {
        try {
            mSocket = IO.socket("http://192.168.70.151:5555") // 115.95.222.206
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun connect() {
        if (!mSocket.connected()) {
            mSocket.connect()
        }
    }

    fun disconnect() {
        if (mSocket.connected()) {
            mSocket.disconnect()
        }
    }

    fun getSocket(): Socket {
        return mSocket
    }
}
