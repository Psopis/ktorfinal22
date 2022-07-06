package com.example.functions

import com.example.directoryObjects.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun deleteRoute(parameters: MarshM){
var e = 9
    transaction {
        var allbusStop = marshruts.select(marshruts.idm eq parameters.id).map{it[marshruts.idost]}
        marshNames.deleteWhere {
            marshNames.id.eq(parameters.id.toString())
        }

        for (busStopWithT in allbusStop){
            busStop.deleteWhere {
                busStop.id eq busStopWithT
            }
        }
            traictori.deleteWhere {
                traictori.idm eq parameters.id.toString()
            }
            marshruts.deleteWhere {
                marshruts.idm eq parameters.id.toString()
            }


    }
}