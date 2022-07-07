package com.example.functions

import com.example.directoryObjects.dbversion
import com.example.directoryObjects.dbversionface
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun UpdateDataBase(){
    transaction {
        val s = dbversion.selectAll().map{ dbversionface(version = it[dbversion.version]) }.first()
        dbversion.update(){ it[version] = s.version + 1 }  }

}