import com.example.directoryObjects.busconnection
import com.example.directoryObjects.geopos
import com.example.plugins.listsOfChannels
import com.example.plugins.mutableSet
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object DriverGenerator{
    var job: Job? = null
    fun connect() {
        for(connection in mutableSet){
            job = GlobalScope.launch {
                delay(5000)
                while (true){
                    connection.channel.consumeEach { value ->
                        if(value is Frame.Text){
                    val geoposition = Json.decodeFromString<geopos>(value.readText())
                        buschannel.emit(busconnection(connection.id.toInt(), value.toString()))
                    }
                    }


                }

            }

        }}
    fun disconnect() {
        job?.cancel()
        job = null
    }
}

val buschannel = MutableSharedFlow<busconnection>(0,1, onBufferOverflow = BufferOverflow.DROP_LATEST)