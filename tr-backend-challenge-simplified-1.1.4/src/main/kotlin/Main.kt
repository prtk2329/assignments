import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import processor.InstrumentEventProcessor
import processor.QuoteEventProcessor
import repository.InstrumentRepository
import repository.QuoteRepository

private val logger = LoggerFactory.getLogger("MainKt")

fun main() {
    logger.info("starting up")

    val instrumentRepository = InstrumentRepository()
    val quoteRepository = QuoteRepository(instrumentRepository)

    val server = Server(quoteRepository = quoteRepository)
    val instrumentStream = InstrumentStream()
    val quoteStream = QuoteStream()


    val instrumentEventProcessor = InstrumentEventProcessor(instrumentRepository)
    val quoteEventProcessor = QuoteEventProcessor(quoteRepository)

    runBlocking {
        launch {
            instrumentEventFlow(instrumentStream).collect { event ->
                instrumentEventProcessor.process(event)
            }
        }

        launch {
            quoteEventFlow(quoteStream).collect { event ->
                quoteEventProcessor.process(event)
            }

        }
        server.start()
    }
}

val jackson: ObjectMapper =
    jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


fun instrumentEventFlow(stream: InstrumentStream) = callbackFlow {
    stream.connect { event ->
        logger.info("{} - Instrument: {}", Thread.currentThread(), event)
        trySend(event)
    }

    awaitClose {
        //TODO - handle WebSocket disconnection here if needed
    }
}

fun quoteEventFlow(stream: QuoteStream) = callbackFlow {
    stream.connect { event ->
        logger.info("{} - Quote: {}", Thread.currentThread(), event)
        trySend(event)
    }

    awaitClose {
        //TODO - handle WebSocket disconnection here if needed
    }
}
