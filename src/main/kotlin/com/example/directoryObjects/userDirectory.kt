package com.example.directoryObjects

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Users : Table("directory") {
    val idnum = varchar("id", 100) // Column<String>
    val name = varchar("name", 100) // Column<String>
    val user_type = varchar("user_Type",6)
}
@Serializable
data class UserM(val id : String = "", val name : String = "", val User_type:String = "")
