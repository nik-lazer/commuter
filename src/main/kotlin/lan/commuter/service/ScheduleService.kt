package lan.commuter.service

import lan.commuter.Station
import lan.commuter.pojo.ThreadOutput
import java.time.LocalDate
import java.util.*
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class ScheduleService(private val searchService: SearchService) {

    fun fetchSchedule(date: LocalDate, startPoint: Station, finalPoint: Station): List<Pair<ThreadOutput?, ThreadOutput?>> {
        val startPointResponse = searchService.load(startPoint, Station.SWITCH_POINT, date)
        val finalPointResponse = searchService.load(Station.SWITCH_POINT, finalPoint, date)
        val startPointThreads = searchService.map(startPointResponse)
        val finalPointThreads = searchService.map(finalPointResponse)
        return connectThreads(startPointThreads, finalPointThreads)
    }

    fun connectThreads(startPointThreads: List<ThreadOutput>, finalPointThreads: List<ThreadOutput>): List<Pair<ThreadOutput?, ThreadOutput?>> {
        val startPointsQueue = LinkedList(startPointThreads)
        val finalPointsQueue = LinkedList(finalPointThreads)
        val result = ArrayList<Pair<ThreadOutput?, ThreadOutput?>>()

        while (startPointsQueue.isNotEmpty() || finalPointsQueue.isNotEmpty()) {
            val thread1 = startPointsQueue.peek()
            val thread2 = finalPointsQueue.peek()
            if (!isBefore(thread1, thread2)) {
                result.add(Pair(null, finalPointsQueue.poll()))
            } else {
                val startThread = startPointsQueue.poll()
                val nextThread1 = startPointsQueue.peek()
                if (!isBefore(nextThread1, thread2)) {
                    result.add(Pair(startThread, finalPointsQueue.poll()))
                } else {
                    result.add(Pair(startThread, null))
                }
            }
        }

        return result
    }

    private fun isBefore(thread: ThreadOutput?, threadToCompare: ThreadOutput?):Boolean {
        if (thread == null && threadToCompare != null) {
            return false
        }
        if (thread != null && threadToCompare == null) {
            return true
        }
        if (thread!= null && threadToCompare!=null) {
            return thread.arrival.isBefore(threadToCompare.departure)
        }
        return false
    }
}