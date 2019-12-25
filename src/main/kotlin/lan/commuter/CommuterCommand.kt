package lan.commuter

import io.micronaut.configuration.picocli.PicocliRunner
import lan.commuter.service.PresentationService
import lan.commuter.service.ScheduleService

import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.time.LocalDate
import java.time.Month
import javax.inject.Inject

@Command(name = "", description = ["..."],
        mixinStandardHelpOptions = true)
class CommuterCommand : Runnable {

    @Option(names = ["-v", "--verbose"], description = ["..."])
    private var verbose : Boolean = false
    @Inject
    private lateinit var scheduleService: ScheduleService
    @Inject
    private lateinit var presentationService: PresentationService

    override fun run() {
        val date = LocalDate.of(2019, Month.DECEMBER, 25)
        val pairs = scheduleService.fetchSchedule(date)
        val text = presentationService.view(pairs)
        presentationService.save("output.html", text)
        if (verbose) {
            println(text)
        }
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            PicocliRunner.run(CommuterCommand::class.java, *args)
        }
    }
}
