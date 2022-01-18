package com.example.directoryObjects


import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object traictori: Table("traiktoria_marshruta"){
    val idm = varchar("id_marsh", 100)
    val lat = double("lat")
    val long = double("long")
}
@Serializable
data class Traictoria(val idm:String = "", val geopos: geopos)
@Serializable
data class geopos(val lat: Double,val long:Double)