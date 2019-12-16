package lan.commuter

import io.micronaut.jackson.ObjectMapperFactory
import lan.commuter.pojo.SearchResponse
import lan.commuter.service.SearchService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class SearchServiceTest {

    @Test
    fun serialize() {
        val fileContent = SearchService::class.java.getResource("/example.json").readText()
        val factory = ObjectMapperFactory()
        val objectMapper = factory.objectMapper(null, null)
        val res = objectMapper.readValue(fileContent, SearchResponse::class.java)

        assertNotNull(res)
        assertEquals(33, res.pagination.total)
        assertEquals(33, res.segments.size)
    }
}