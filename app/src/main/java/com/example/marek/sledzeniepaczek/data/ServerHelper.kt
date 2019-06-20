package com.example.marek.sledzeniepaczek.data

class ServerHelper {

    fun convertToResponses(input: String): ArrayList<ServerResponse> {
        val result = arrayListOf<ServerResponse>()
        val rows = input.split("<br/>")
        for (row in rows) {
            if (row.isNotEmpty()) {
                val splittedRow = row.split(",")
                val location = splittedRow[0].replace("<br/>", "")
                val status = splittedRow[1].replace("<br/>", "")
                val time = splittedRow[2].replace("<br/>", "")
                val response = ServerResponse(location, status, time)
                result.add(response)
            }
        }
        return result
    }
}