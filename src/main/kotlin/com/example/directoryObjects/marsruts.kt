package com.example

import com.example.directoryObjects.bustoptimewith
import com.example.directoryObjects.geopos
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object marshruts : Table("marsruts"){
    val id = integer("id").autoIncrement().uniqueIndex()
    val idm = varchar("idm",100)
    val idost = varchar("idost",100)
    val idostplus = varchar("idostplus",100)
    val clock = varchar("timer",100)
    val id_traictori = varchar("id_traictori",30)
}
@Serializable
data class MarshrutM(val id: String = "", val idm_Foreign: String = "", val idOst:List<bustoptimewith> = listOf<bustoptimewith>(), val lineMarshtriectori :List<geopos>, val clock:String = "", val idtraictori:String = "")
@Serializable
data class MarshMTime(val id : String = "",val clock:String = "")
@Serializable
data class MarshM(val id: String = "", val idOst:String)
