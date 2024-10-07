package service.impl

import Candlestick
import Quote
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import java.time.Instant
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import repository.QuoteRepository
import service.CandlestickManager

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CandleStickManagerImplTest(@MockK private val repository: QuoteRepository) {
    private lateinit var candleStickManager: CandlestickManager

    private val quote1 = Quote("XYZ1234", 12.121, Instant.parse("2019-03-05T12:58:00.645391Z"))
    private val quote2 = Quote("XYZ1234", 12.502, Instant.parse("2019-03-05T12:58:05.645391Z"))
    private val quote3 = Quote("XYZ1234", 12.503, Instant.parse("2019-03-05T12:58:59.645391Z"))
    private val quote4 = Quote("XYZ1234", 12.073, Instant.parse("2019-03-05T12:59:00.645391Z"))

    @BeforeEach
    fun setup() {
        candleStickManager = CandleStickManagerImpl(repository)

        val quotesByTimestamp = mapOf(
            Instant.parse("2019-03-05T12:58:00Z") to listOf(quote1, quote2, quote3),
            Instant.parse("2019-03-05T12:59:00Z") to listOf(quote4)
        )

        every { repository.getQuotesFromTimestamp("XYZ123", any(), any()) } returns emptyMap()
        every { repository.getQuotesFromTimestamp("XYZ124", any(), any()) } returns quotesByTimestamp
        every { repository.findPreviousQuote("XYZ124", any()) } returns null
    }

    @Test
    fun testShouldReturnCandlestick() {
        val expectedCandlesticks = listOf(
            Candlestick(
                openTimestamp = Instant.parse("2019-03-05T12:58:00Z"),
                closeTimestamp = Instant.parse("2019-03-05T12:59:00Z"),
                openPrice = 12.121,
                closingPrice = 12.503,
                highPrice = 12.503,
                lowPrice = 12.121
        ),
            Candlestick(
                openTimestamp = Instant.parse("2019-03-05T12:59:00Z"),
                closeTimestamp = Instant.parse("2019-03-05T13:00:00Z"),
                openPrice = 12.073,
                closingPrice = 12.073,
                highPrice = 12.073,
                lowPrice = 12.073
            ),
        )
        val candlesticks = candleStickManager.getCandlesticks ("XYZ124")

        assertFalse(candlesticks.isEmpty())
        assertEquals(2, candlesticks.size)
        assertEquals(expectedCandlesticks, candlesticks)

        verify(exactly = 1) { repository.getQuotesFromTimestamp("XYZ124", any(), any()) }
    }

    @Test
    fun testShouldReturnNoCandlestickWhenInstrumentNotExists() {
        assertEquals(emptyList<Candlestick>(), candleStickManager.getCandlesticks ("XYZ123"))
        verify(exactly = 1) { repository.getQuotesFromTimestamp("XYZ123", any(), any()) }
    }
}
