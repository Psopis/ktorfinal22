package com.example.plugins

import buschannel
import com.example.directoryObjects.DriverConnection
import com.example.directoryObjects.busStop
import com.example.directoryObjects.busconnection
import com.example.directoryObjects.geopos
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.select
import java.time.Duration
import java.util.*

var listsOfChannels = Collections.synchronizedMap<Int, BroadcastChannel<Frame>>(mutableMapOf())


var mutableSet = Collections.synchronizedList<DriverConnection>(mutableListOf())
fun Application.configureSockets() {




    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false

    }



    routing {
        webSocket("/all") {

            GlobalScope.launch(Dispatchers.Default) {
                buschannel.collect(){ response ->

                    send(response.toString())
                    delay(3000)
                }
            }
            while (true) {
                delay(2000)
                if (mutableSet != null) {
                    DriverGenerator.connect()
                } else {
                    DriverGenerator.disconnect()
                }
            }
        }

        webSocket("/passenger/{idpassenger}") {
            val idpassenger = call.parameters["idpassenger"].toString()
            if (idpassenger == null) close()

            launch {
                var driver = mutableSet.find { idpassenger == it.id } ?: DriverConnection()


                println("driver $driver")
                driver.let {
                    it.channel.consumeEach { frame ->
                        send(frame.copy())
                        println("frame.copy()  ${frame.copy()}")
                    }
                }

            }.join()

        }
        webSocket("/driver/{idDriver}") {
            val idDriver = call.parameters["idDriver"]
            if (idDriver == null) close(CloseReason(CloseReason.Codes.NORMAL, "Driver is null"))
            else {
//            val broadcastChannel = Channel<Frame>()
//            listsOfChannels.put(idDriver, broadcastChannel.broadcast())
                var driver = mutableSet.find { idDriver == it.id }
                if (driver != null) {
                    driver.connection = this
                } else {
                    driver = DriverConnection(idDriver, this)
                    driver.connection = this
                    mutableSet.add(driver)
                }
                driver.startBroadcast()
            }
        }

    }
}

