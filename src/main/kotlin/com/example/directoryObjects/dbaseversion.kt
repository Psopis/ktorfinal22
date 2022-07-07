package com.example.directoryObjects

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object dbversion: Table("database_version"){
    val version = integer("version")

}

@Serializable
data class  dbversionface(val version: Int = 0)