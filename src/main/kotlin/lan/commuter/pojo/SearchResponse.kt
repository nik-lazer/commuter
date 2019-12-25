package lan.commuter.pojo

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class SearchResponse(
        val pagination: Pagination,
        val segments: List<Segment>
)

data class Pagination(
    val total: Int,
    val limit: Int,
    val offset: Int
)

data class Segment(
        val arrival: Date,
        val departure: Date,
        val thread: Thread,
        val stops: String,
        val duration: Int,
        @param:JsonProperty("tickets_info") val ticketsInfo: TicketsInfo?
//val price: Double
)

data class TicketsInfo(
        val places: List<Place>
)

data class Thread(
        val title: String,
        val number: String,
        @param:JsonProperty("express_type") val expressType: String?
)

data class Place(
        val price: Price
)

data class Price(
        val whole: Int
)

