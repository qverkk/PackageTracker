package com.example.marek.sledzeniepaczek.data

import java.io.Serializable

class ServerResponse(val location: String, val status: String, val time: String) : Serializable {

    fun prettyPrint(): String {
        return "Location: $location, status: $status, time: $time"
    }
}