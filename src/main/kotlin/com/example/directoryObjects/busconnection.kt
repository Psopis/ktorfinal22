package com.example.directoryObjects

import kotlinx.serialization.Serializable
import java.text.FieldPosition

@Serializable
data class busconnection (
    val id:String? = "",
    val position : geopos = geopos(0.0,0.0)
//val position: String = ""
)

