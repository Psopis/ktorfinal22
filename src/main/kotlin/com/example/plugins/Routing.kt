package com.example.plugins

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.utils.io.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.javatime.time
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

import java.sql.Time
object  busStop :Table("busstop"){
    val id = varchar("id",20)
    val name = varchar("name",30)
    val lat = float("lat")
    val lng = float("long")
}


object marshruts :Table("marsruts"){
    val id = varchar("id",20)
    val idm = varchar("idm",20)
    val idost = varchar("idost",20)
    val idostplus = varchar("idostplus",20)
    val clock = varchar("timer",10)
}
object marshNames :Table("table_name"){
    val id = varchar("id",20)
    val name = varchar("name",30)
}
object Users : Table("directory") {
    val idnum = varchar("idNum",20) // Column<String>
    val name = varchar("name", 20) // Column<String>
    val user_type = varchar("user_Type",6)
}
@Serializable
data class geopos(val lat: Float,val long: Float)
@Serializable
data class busstposCorrestion(val id :String ="",val first:List<String>,)
@Serializable
data class  BusStopM(val id: String = "",val name:String = "",val geopos :geopos)
@Serializable
data class BusStopMplusClock(val first:BusStopM,val second:MarshMTime)

@Serializable
data class  MarshNamesM(val id: String = "",val name:String = "")


@Serializable
data class UserM(val id : String = "", val name : String = "", val User_type:String = "")


@Serializable
data class MarshrutM(val id: String = "", val idm_Foreign: String = "", val idOst:List<BusStopMplusClock> ,val lineMarshtriectori :List<geopos> , val clock:String = "")
@Serializable
data class MarshMTime(val id : String = "",val clock:String = "")

fun Application.configureRouting() {

    routing {
        get("/{id}") {
            var ids = call.parameters["id"]?.toInt() ?: 0

            val a = transaction {
                Users.selectAll().map {
                    (UserM(it[Users.idnum], it[Users.name],it[Users.user_type]))

                }
            }
            call.respondText(Json.encodeToString<UserM>(a[ids]), ContentType.Application.Json)
        }

        get("/sign/{id_sign}") {
            val sigin_id = call.parameters["id_sign"]

            val a = transaction {


                val t = Users.select(){
                    Users.idnum eq sigin_id.toString()
                }.find { it[Users.idnum] == sigin_id.toString()}

                t?.get(Users.idnum)?.let{it2 -> UserM(it2,t[Users.name],t[Users.user_type])}
            }


            call.respondText(Json.encodeToString<UserM?>(a), ContentType.Application.Json)
        }

        get ("/namesMarsh"){
            val a = transaction {
                marshNames.selectAll().map{
                    (MarshNamesM(it[marshNames.id],it[marshNames.name]))

                }

            }
            call.respondText(Json.encodeToString<List<MarshNamesM>>(a), ContentType.Application.Json)
        }
//        get("/OneMarshName/{id}"){
//            val res = call.parameters["id"]?.toInt() ?: 0
//            val a = transaction {
//                val t = marshNames.select(){
//                    marshNames.id eq res.toString()
//                }.find{it[marshNames.id] == res.toString()}
//            }
//
//        }











        get ("/OneMarsh/{id}") {
            val res = call.parameters["id"]?.toString() ?: ""
            val l = mutableListOf<busstposCorrestion>()
            val li = mutableListOf<List<String>>()
            val a = transaction {
                val marshname = marshNames.select(marshNames.id eq res).map{it[marshNames.name]}.first()
                val marshid = marshNames.select(marshNames.name eq marshname).map{it[marshNames.id]}.first()

                marshruts.select(marshruts.idm eq res).map{ l.add((busstposCorrestion(it[marshruts.id],listOf(it[marshruts.idost],it[marshruts.idostplus]))))}
                l.sortBy { it.id }
                l.forEach { li.add(it.first)}

                var list = li.flatten().distinct()
                var lis = mutableListOf<BusStopMplusClock>()
                var listGeoPosFalse = mutableListOf<List<geopos>>()

print(list)
                for(i in list){
                   var fir = busStop.select(busStop.id eq i).map {BusStopM(it[busStop.id], it[busStop.name],geopos(it[busStop.lat],it[busStop.lng])) }.first()
                   var sec = marshruts.select(marshruts.idm eq res).map { MarshMTime(clock = it[marshruts.clock]) }.first()
print(sec)
                    print(fir)
                   lis.add(BusStopMplusClock(fir,sec))
                }

                for(i in list){
                    listGeoPosFalse.add(busStop.select(busStop.id eq i).map { geopos(it[busStop.lat],it[busStop.lng]) })

                }


                var listGeoPosTrue = listGeoPosFalse.flatten()
                var listof = lis










                marshruts.select(marshruts.idm eq res).map{ (MarshrutM(marshid,marshname,listof,listGeoPosTrue)) }.first()


            }


            call.respondText(Json.encodeToString<MarshrutM>(a), ContentType.Application.Json)

        }













        get("/marsh") {
            val result = listsOfChannels.keys.toList()


            call.respondText(Json.encodeToString<List<Int>>(result), ContentType.Application.Json)


        }


    }


}