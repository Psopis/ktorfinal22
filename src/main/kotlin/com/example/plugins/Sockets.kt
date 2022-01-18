package com.example.plugins

import com.example.directoryObjects.DriverConnection
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.*
var listsOfChannels = Collections.synchronizedMap<Int, BroadcastChannel<Frame>>(mutableMapOf())
var connection:DefaultWebSocketSession? = null
var websocId:String = ""
var defaultWebsocket = mutableMapOf<String,DefaultWebSocketSession?>()
var mutableSet = Collections.synchronizedList<DriverConnection>(mutableListOf())
fun Application.configureSockets() {



    val stateflow: MutableStateFlow<String> = MutableStateFlow("")

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }



    routing {

       webSocket("/passenger/{idpassenger}") {
            val idpassenger = call.parameters["idpassenger"]
           if (idpassenger == null) close()

           launch{
               val driver = mutableSet.find { idpassenger == it.id }

               driver?.let {
                   it.channel.consumeEach { frame ->
                       send(frame.copy())
                   }
               }

            }.join()

        }
       webSocket("/driver/{idDriver}") {
            val idDriver = call.parameters["idDriver"]
           if (idDriver == null) close()

//            val broadcastChannel = Channel<Frame>()
//            listsOfChannels.put(idDriver, broadcastChannel.broadcast())
           val driver = mutableSet.find { idDriver == it.id }
           if (driver != null){
               driver.connection = this
               driver.startBroadcast()
           } else{
               val newdriver = DriverConnection(idDriver!!, this)
               newdriver.connection = this
               newdriver.startBroadcast()
               mutableSet.add(newdriver)
           }


        }



    }
}

