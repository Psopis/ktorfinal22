package com.example.functions

import com.example.directoryObjects.MarshrutM
import com.example.directoryObjects.busStop
import com.example.directoryObjects.marshNames
import com.example.directoryObjects.traictori
import com.example.directoryObjects.marshruts
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun addRoute(parameters: MarshrutM, liststops:MutableList<String>, timer: MutableList<String>){
    var x = 0
    transaction {
        marshNames.insert {
            it[id] = parameters.id.toString()
            it[name] = parameters.idm_Foreign
        }
        for (busStopWithT in parameters.idOst){
            busStop.insert {
                it[id] = busStopWithT.first.id
                it[name] = busStopWithT.first.name
                it[lat] = busStopWithT.first.geopos.lat
                it[lng] = busStopWithT.first.geopos.long
            }
        }
        for(point in parameters.lineMarshtriectori){
            traictori.insert {
                it[idm] = parameters.id.toString()
                it[lat] = point.lat
                it[long] = point.long
            }
        }
        var idroutes = marshruts.selectAll().map { it[marshruts.id] }.maxOrNull()!!
        for(stop in liststops.windowed(2)){
            idroutes += 1

            marshruts.insert {
                it[id] = idroutes
                it[idm] = parameters.id.toString()
                it[idost] = stop.first().toString()
                it[idostplus] = stop.last().toString()
                it[clock] = timer[x]
                it[id_traictori] = parameters.id.toString()
            }
            x += 1
        }
        var laststop = liststops.last()
        marshruts.insert {
            it[id] = idroutes+1
            it[idm] = parameters.id.toString()
            it[idost] = laststop
            it[idostplus] = laststop
            it[clock] = ""
            it[id_traictori] = parameters.id.toString()
        }

    }
}