package lan.commuter.service

import lan.commuter.pojo.ThreadOutput
import java.io.File
import javax.inject.Singleton

@Singleton
class PresentationService {
    fun view(pairs: List<Pair<ThreadOutput?, ThreadOutput?>>): String {
        val header = """<meta content="text/html; charset=UTF-8" http-equiv="Content-Type"><html><body><table border="1">"""
        val footer = "</table></body></html>"
        var body = ""
        pairs.forEach {
            val startNumber = it.first?.number ?: ""
            val startTitle = (it.first?.title ?: "") + addType(it.first)
            val startDeparture = it.first?.departure ?: ""
            val startDuration = it.first?.duration ?: ""
            val startArrival = it.first?.arrival ?: ""
            val startComments = it.first?.stops ?: ""

            val finalNumber = it.second?.number ?: ""
            val finalTitle = (it.second?.title ?: "") + addType(it.second)
            val finalDeparture = it.second?.departure ?: ""
            val finalDuration = it.second?.duration ?: ""
            val finalArrival = it.second?.arrival ?: ""
            val finalComments = it.second?.stops ?: ""

            body += """
<tr>
<td>$startNumber<td>
<td>$startTitle<td>
<td>$startDeparture<td>
<td>$startDuration<td>
<td>$startArrival<td>
<td>$startComments<td>

<td>$finalNumber<td>
<td>$finalTitle<td>
<td>$finalDeparture<td>
<td>$finalDuration<td>
<td>$finalArrival<td>
<td>$finalComments<td>
""".trimIndent()
        }
        return header + body + footer
    }

    fun save(fileName: String, content: String) {
        File(fileName).writeText(content)
    }

    private fun addType(threadOutput: ThreadOutput?): String {
        return if (threadOutput?.type != null)
            " (${threadOutput.type})"
        else
            ""
    }
}