package lan.commuter.service

import io.micronaut.context.annotation.Value
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import lan.commuter.Station
import lan.commuter.pojo.SearchResponse
import lan.commuter.pojo.Segment
import lan.commuter.pojo.ThreadOutput
import lan.commuter.pojo.TicketsInfo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Singleton
import javax.inject.Inject

@Singleton
class SearchService(
        @Value("\${commuter.api.search.url}") val searchUrl: String,
        @Value("\${commuter.api.key}") val key: String
) {
    @field:Client("\${commuter.api.search.url}")
    @Inject
    lateinit var httpClient: RxHttpClient

    fun load(from: Station, to: Station, date: LocalDate): SearchResponse {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = date.format(formatter)
        val uri = UriBuilder.of(searchUrl)
                .queryParam("apikey", key)
                .queryParam("from", from.code)
                .queryParam("to", to.code)
                .queryParam("transport_type", "train")
                .queryParam("date", formattedDate)
                .toString()
        println(uri)
        return httpClient.toBlocking().retrieve(uri, SearchResponse::class.java)
    }

    fun map(searchResponse: SearchResponse): List<ThreadOutput> {
        return searchResponse.segments.map {
            mapThread(it)
        }.toList()
    }

    private fun mapThread(it: Segment): ThreadOutput {
        val departureTime = fromDate(it.departure)
        val arrivalTime = fromDate(it.arrival)
        val price = fetchPrice(it.ticketsInfo)
        return ThreadOutput(
                it.thread.number,
                departureTime,
                arrivalTime,
                it.duration/60,
                it.thread.title,
                it.stops,
                price
        )

    }

    private fun fetchPrice(ticketsInfo: TicketsInfo?): Int {
        if (ticketsInfo != null) {
            return ticketsInfo.places[0].price.whole
        }
        return 0
    }

    private fun fromDate(date: Date): LocalTime {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime()
    }
}