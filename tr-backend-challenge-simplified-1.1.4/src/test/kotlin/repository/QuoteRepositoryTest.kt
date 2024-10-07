package repository

import Instrument
import Quote
import java.time.Instant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.minusMinutes
import kotlin.test.assertEquals

internal class QuoteRepositoryTest {
    private lateinit var instrumentRepository: InstrumentRepository
    private lateinit var quoteRepository: QuoteRepository

    private val quote1 = Quote("XYZ1234", 12.121, Instant.parse("2019-03-05T12:58:00.645391Z"))
    private val quote2 = Quote("XYZ1234", 12.502, Instant.parse("2019-03-05T12:58:05.645391Z"))
    private val quote3 = Quote("XYZ1234", 12.503, Instant.parse("2019-03-05T12:58:59.645391Z"))
    private val quote4 = Quote("XYZ1234", 12.073, Instant.parse("2019-03-05T12:59:00.645391Z"))

    @BeforeEach
    fun setup() {
        instrumentRepository = InstrumentRepository()
        quoteRepository = QuoteRepository(instrumentRepository)

        val instrument1 = Instrument("XYZ1234", "Add XYZ1234 Instrument")
        val instrument2 = Instrument("XYZ1235", "Add XYZ1235 Instrument")

        instrumentRepository.add(instrument1)
        instrumentRepository.add(instrument2)

        quoteRepository.add(quote1)
        quoteRepository.add(quote2)
        quoteRepository.add(quote3)
        quoteRepository.add(quote4)
    }

    @Test
    fun testShouldAddQuoteSuccessfully() {
        val endRange = Instant.parse("2019-03-05T12:59:59.645391Z")

        val quote5 = Quote("XYZ1234", 12.03, Instant.parse("2019-03-05T12:59:09.645391Z"))
        quoteRepository.add(quote5)

        val expected = mapOf(
            Instant.parse("2019-03-05T12:58:00Z") to listOf(quote1, quote2, quote3),
            Instant.parse("2019-03-05T12:59:00Z") to listOf(quote4, quote5)
        )

        val actual = quoteRepository
            .getQuotesFromTimestamp("XYZ1234", minusMinutes(endRange, 5), endRange)

        assertEquals(expected, actual)
    }

    @Test
    fun testShouldNotAddQuoteWhenInstrumentNotExists() {
        val endRange = Instant.parse("2019-03-05T12:59:59.645391Z")
        val quote1 = Quote("XYZ12349", 12.121, Instant.parse("2019-03-05T12:58:00.645391Z"))
        quoteRepository.add(quote1)

        val actual = quoteRepository
            .getQuotesFromTimestamp("XYZ12349", minusMinutes(endRange, 5), endRange)

        assertEquals(emptyMap(), actual)
    }

    @Test
    fun testShouldRemoveAllQuotesOfInstrument() {
        val endRange = Instant.parse("2019-03-05T12:59:59.645391Z")

        quoteRepository.remove("XYZ1234")

        val actual = quoteRepository
            .getQuotesFromTimestamp("XYZ1234", minusMinutes(endRange, 5), endRange)

        assertEquals(emptyMap(), actual)
    }

    @Test
    fun shouldReturnAllQuotesForGivenTimestamp() {
        val endRange = Instant.parse("2019-03-05T12:59:59.645391Z")

        val expected = mapOf(
            Instant.parse("2019-03-05T12:58:00Z") to listOf(quote1, quote2, quote3),
            Instant.parse("2019-03-05T12:59:00Z") to listOf(quote4)
        )

        val actual = quoteRepository
            .getQuotesFromTimestamp("XYZ1234", minusMinutes(endRange, 5), endRange)

        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnPreviousQuoteForGivenTimestamp() {
        val expected = Pair(Instant.parse("2019-03-05T12:59:00Z"), listOf(quote4))

        val actual = quoteRepository
            .findPreviousQuote("XYZ1234", Instant.parse("2019-03-05T13:02:59.645391Z"))

        assertEquals(expected, actual)
    }
}
