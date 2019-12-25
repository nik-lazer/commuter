package lan.commuter.service

import lan.commuter.pojo.ThreadOutput
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertAll
import java.time.LocalTime

internal class ScheduleServiceTest {
    private lateinit var scheduleService: ScheduleService

    @BeforeEach
    internal fun setUp() {
        val searchService = SearchService("", "")
        scheduleService = ScheduleService(searchService)
    }

    @Test
    fun connectThreads() {
        // given
        val startThread1 = createThread("1->2.1", LocalTime.of(6, 30, 0), LocalTime.of(7, 0, 0))
        val startThread2 = createThread("1->2.2", LocalTime.of(7, 0, 0), LocalTime.of(7, 30, 0))
        val startThread3 = createThread("1->2.3", LocalTime.of(7, 30, 0), LocalTime.of(8, 0, 0))
        val startThread4 = createThread("1->2.4", LocalTime.of(8, 0, 0), LocalTime.of(8, 30, 0))

        val finalThread1 = createThread("2->3.1", LocalTime.of(6, 40, 0), LocalTime.of(6, 50, 0))
        val finalThread2 = createThread("2->3.2", LocalTime.of(7, 20, 0), LocalTime.of(7, 30, 0))
        val fianlThread3 = createThread("2->3.3", LocalTime.of(8, 10, 0), LocalTime.of(8, 20, 0))

        val startPointThreads = listOf(startThread1, startThread2, startThread3, startThread4)
        val finalPointThreads = listOf(finalThread1, finalThread2, fianlThread3)

        // when
        val result = scheduleService.connectThreads(startPointThreads, finalPointThreads)

        // then
        assertEquals(5, result.size)
        assertAll(
                {assertNull(result[0].first)},
                {assertEquals("2->3.1", result[0].second?.number)}
        )
        assertAll(
                {assertEquals("1->2.1", result[1].first?.number)},
                {assertEquals("2->3.2", result[1].second?.number)}
        )
        assertAll(
                {assertEquals("1->2.2", result[2].first?.number)},
                {assertNull(result.get(2).second)}
        )
        assertAll(
                {assertEquals("1->2.3", result[3].first?.number)},
                {assertEquals("2->3.3", result[3].second?.number)}
        )
        assertAll(
                {assertEquals("1->2.4", result[4].first?.number)},
                {assertNull(result[4].second)}
        )
    }

    private fun createThread(number: String, departure: LocalTime, arrival: LocalTime): ThreadOutput {
        return ThreadOutput(number, departure, arrival, 0, "", "", 0)
    }
}