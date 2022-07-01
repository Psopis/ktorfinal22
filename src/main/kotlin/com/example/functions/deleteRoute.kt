package com.example.functions

import com.example.directoryObjects.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun deleteRoute(parameters: MarshrutM){
var e = 9
    transaction {
        marshNames.deleteWhere {
            marshNames.id.eq(parameters.id.toString())
        }
        for (busStopWithT in parameters.idOst){
            busStop.deleteWhere {
                busStop.id eq busStopWithT.first.id
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