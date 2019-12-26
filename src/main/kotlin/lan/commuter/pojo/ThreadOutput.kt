package lan.commuter.pojo

import java.time.LocalTime

data class ThreadOutput(
        val number: String,
        val departure: LocalTime,
        val arrival: LocalTime,
        val duration: Int,
        val title: String,
        val stops: String,
        val price: Int,
        val type: String?
)