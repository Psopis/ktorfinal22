package com.example.functions

import com.example.MarshrutM
import com.example.directoryObjects.busStop
import com.example.directoryObjects.marshNames
import com.example.directoryObjects.traictori
import com.example.marshruts
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

fun deleteRoute(parameters: MarshrutM){

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