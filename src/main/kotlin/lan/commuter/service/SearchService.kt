package lan.commuter

import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import lan.commuter.pojo.SearchResponse
import lan.commuter.pojo.Segment
import lan.commuter.pojo.ThreadOutput
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.function.Consumer
import javax.inject.Singleton
import javax.inject.Inject

@Singleton
class SearchService() {
    @field:Client("\${commuter.api.search.url}")
    @Inject
    lateinit var httpClient: RxHttpClient

    fun load(from: Station, to: Station, date: LocalDate): SearchResponse {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var formattedDate = date.format(formatter)
        val uri = UriBuilder.of("https://api.rasp.yandex.net/v3.0/search/")
                .queryParam("apikey", "64ba449f-587e-431f-b831-e837192100d0")
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
        return ThreadOutput(
                it.thread.number,
                LocalTime.ofInstant(it.departure.toInstant(), ZoneId.systemDefault()),
                LocalTime.ofInstant(it.arrival.toInstant(), ZoneId.systemDefault()),
                it.duration,
                it.thread.title,
                it.ticketsInfo.places.get(0).price.whole
        )

    }
}