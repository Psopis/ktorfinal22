package com.example.directoryObjects

import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.launch

class DriverConnection(
    var id:String = "" ,
    var connection: DefaultWebSocketSession? = null,
    val channel : BroadcastChannel<Frame> = BroadcastChannel<Frame>(1)//Channel<Frame>().broadcast()
){
    suspend fun startBroadcast(){
        connection?.let { connect ->
            for (frame in connect.incoming) {
               println(this)
                    channel.send(frame.copy())

            }
        }
    }
}