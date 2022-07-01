package com.example.directoryObjects


import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object  busStop : Table("busstop"){
    val id = varchar("id",100)
    val name = varchar("name",100)
    val lat = double("lat")
    val lng = double("long")
}

@Serializable
data class  BusStopM(val id: String = "",val name:String = "",val geopos : geopos)
@Serializable
data class bustoptimewith(val first :BusStopM,val second:String="")
@Serializable
data class busstposCorrestion(val id :Int = 0,val first:List<String>,)
@Serializable
data class busStopID(val id:String = "")