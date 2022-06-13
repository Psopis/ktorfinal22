package com.example.directoryObjects

import kotlinx.serialization.Serializable

@Serializable
data class busconnection (
    val id:Int? = 0,
    val position : String
)

