package service

import Candlestick
import Quote
import java.time.Instant
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class AggregateQuotesTest {

    companion object {
        private val quote0 = Quote("foo", 10.10, Instant.parse("2019-03-05T12:56:05Z"))

        private val quote1 = Quote("foo", 10.0, Instant.parse("2019-03-05T13:00:05Z"))
        private val quote2 = Quote("foo", 11.0, Instant.parse("2019-03-05T13:00:06Z"))
        private val quote3 = Quote("foo", 15.0, Instant.parse("2019-03-05T13:00:13Z"))
        private val quote4 = Quote("foo", 11.0, Instant.parse("2019-03-05T13:00:19Z"))
        private val quote5 = Quote("foo", 13.0, Instant.parse("2019-03-05T13:00:32Z"))
        private val quote6 = Quote("foo", 12.0, Instant.parse("2019-03-05T13:00:49Z"))
        private val quote7 = Quote("foo", 12.0, Instant.parse("2019-03-05T13:00:57Z"))

        private val quote8 = Quote("foo", 9.0, Instant.parse("2019-03-05T13:01:00Z"))

        private val quote9 = Quote("foo", 9.90, Instant.parse("2019-03-05T13:03:00Z"))
        private val quote10 = Quote("foo", 9.27, Instant.parse("2019-03-05T13:03:10Z"))
        private val quote11 = Quote("foo", 9.99, Instant.parse("2019-03-05T13:03:40Z"))
        private val quote12 = Quote("foo", 9.93, Instant.parse("2019-03-05T13:03:59Z"))

        private val quotes_13_00 = mapOf(
            Instant.parse("2019-03-05T13:00:00Z")
                    to listOf(quote1, quote2, quote3, quote4, quote5, quote6, quote7)
        )
        private val quotes_13_01 = mapOf(
            Instant.parse("2019-03-05T13:01:00Z")
                    to listOf(quote8)
        )
        private val quotes_13_03 = mapOf(
            Instant.parse("2019-03-05T13:03:00Z")
                    to listOf(quote9, quote10, quote11, quote12)
        )

        val previousQuotesByTimestamp = Pair(Instant.parse("2019-03-05T12:56:00Z"), listOf(quote0))
    }

    @Test
    fun testAggregateQuotesByMinuteShouldReturnCorrectCandlesticks() {
        val expectedCandlestick0 = Candlestick(
            openTimestamp = Instant.parse("2019-03-05T13:00:00Z"),
            closeTimestamp = Instant.parse("2019-03-05T13:01:00Z"),
            openPrice = 10.00,
            closingPrice = 12.00,
            lowPrice = 10.00,
            highPrice = 15.00
        )

        val expectedCandlestick1 = Candlestick(
            openTimestamp = Instant.parse("2019-03-05T13:01:00Z"),
            closeTimestamp = Instant.parse("2019-03-05T13:02:00Z"),
            openPrice = 9.00,
            closingPrice = 9.00,
            lowPrice = 9.00,
            highPrice = 9.00
        )

        val quotes = quotes_13_00.plus(quotes_13_01)
        val startsFrom = Instant.parse("2019-03-05T13:00:00Z")

        val result = aggregateQuotes(quotes, startsFrom, null)

        assertEquals(2, result.size)
        assertEquals(expectedCandlestick0, result[0])
        assertEquals(expectedCandlestick1, result[1])
    }

    @Test
    fun testShouldAddMultipleMissingCandlesticksInBeginning() {
        val expectedCandlestick0 = Candlestick(
            openTimestamp = Instant.parse("2019-03-05T12:58:00Z"),
            closeTimestamp = Instant.parse("2019-03-05T12:59:00Z"),
            openPrice = 10.10,
            closingPrice = 10.10,
            lowPrice = 10.10,
            highPrice = 10.10
        )

        val expectedCandlestick1 = Candlestick(
            openTimestamp = Instant.parse("2019-03-05T12:59:00Z"),
            closeTimestamp = Instant.parse("2019-03-05T13:00:00Z"),
            openPrice = 10.10,
            closingPrice = 10.10,
            lowPrice = 10.10,
            highPrice = 10.10
        )

        val expectedCandlestick2 = Candlestick(
            openTimestamp = Instant.parse("2019-03-05T13:00:00Z"),
            closeTimestamp = Instant.parse("2019-03-05T13:01:00Z"),
            openPrice = 10.00,
            closingPrice = 12.00,
            lowPrice = 10.00,
            highPrice = 15.00
        )

        val expectedCandlestick3 = Candlestick(
            openTimestamp = Instant.parse("2019-03-05T13:01:00Z"),
            closeTimestamp = Instant.parse("2019-03-05T13:02:00Z"),
            openPrice = 9.00,
            closingPrice = 9.00,
            lowPrice = 9.00,
            highPrice = 9.00
        )

        val startsFrom = Instant.parse("2019-03-05T12:58:00Z")
        val quotes = quotes_13_00.plus(quotes_13_01)


        val result = aggregateQuotes(quotes, startsFrom, previousQuotesByTimestamp)

        assertEquals(4, result.size)

        // Verify missing candlestick of 12:58
        assertEquals(expectedCandlestick0, result[0])

        // Verify missing Candlestick of 12:59
        assertEquals(expectedCandlestick1, result[1])

        // Verify candlestick of 13:00
        assertEquals(expectedCandlestick2, result[2])

        // Verify candlestick of 13:01
        assertEquals(expectedCandlestick3, result[3])
    }

    @Test
    fun testShouldAddMultipleMissingCandlesticks() {
        val expectedCandlestick0 = Candlestick(
            openTimestamp = Instant.parse("2019-03-05T12:58:00Z"),
            closeTimestamp = Instant.parse("2019-03-05T12:59:00Z"),
            openPrice = 10.10,
            closingPrice = 10.10,
            lowPrice = 10.10,
            highPrice = 10.10
        )

        val expectedCandlestick1 = Candlestick(
            openTimestamp = Instant.parse("2019-03-05T12:59:00Z"),
            closeTimestamp = Instant.parse("2019-03-05T13:00:00Z"),
            openPrice = 10.10,
            closingPrice = 10.10,
            lowPrice = 10.10,
            highPrice = 10.10
        )

        val expectedCandlestick2 = Candlestick(
            openTimestamp = Instant.parse("2019-03-05T13:00:00Z"),
            closeTimestamp = Instant.parse("2019-03-05T13:01:00Z"),
            openPrice = 10.00,
            closingPrice = 12.00,
            lowPrice = 10.00,
            highPrice = 15.00
        )

        val expectedCandlestick3 = Candlestick(
            openTimestamp = Instant.parse("2019-03-05T13:01:00Z"),
            closeTimestamp = Instant.parse("2019-03-05T13:02:00Z"),
            openPrice = 10.00,
            closingPrice = 12.00,
            lowPrice = 10.00,
            highPrice = 15.00
        )

        val expectedCandlestick4 = Candlestick(
            openTimestamp = Instant.parse("2019-03-05T13:02:00Z"),
            closeTimestamp = Instant.parse("2019-03-05T13:03:00Z"),
            openPrice = 10.00,
            closingPrice = 12.00,
            lowPrice = 10.00,
            highPrice = 15.00
        )

        val expectedCandlestick5 = Candlestick(
            openTimestamp = Instant.parse("2019-03-05T13:03:00Z"),
            closeTimestamp = Instant.parse("2019-03-05T13:04:00Z"),
            openPrice = 9.90,
            closingPrice = 9.93,
            lowPrice = 9.27,
            highPrice = 9.99
        )


        val startsFrom = Instant.parse("2019-03-05T12:58:00Z")
        val quotes = quotes_13_00.plus(quotes_13_03)

        val result = aggregateQuotes(quotes, startsFrom, previousQuotesByTimestamp)

        assertEquals(6, result.size)
        assertEquals(expectedCandlestick0, result[0]) // Verify missing Candlestick of 12:58
        assertEquals(expectedCandlestick1, result[1]) // Verify missing candlestick of 12:59
        assertEquals(expectedCandlestick2, result[2]) // Verify candlestick of 13:00
        assertEquals(expectedCandlestick3, result[3]) // Verify missing candlestick of 13:01
        assertEquals(expectedCandlestick4, result[4]) // Verify missing candlestick of 13:02
        assertEquals(expectedCandlestick5, result[5]) // Verify candlestick of 13:03
    }

    @Test
    fun testShouldNotAddMissingCandlesticksWhenPrevCandlestickIsMissing() {
        val expectedCandlestick0 = Candlestick(
            openTimestamp = Instant.parse("2019-03-05T13:00:00Z"),
            closeTimestamp = Instant.parse("2019-03-05T13:01:00Z"),
            openPrice = 10.00,
            closingPrice = 12.00,
            lowPrice = 10.00,
            highPrice = 15.00
        )

        val expectedCandlestick1 = Candlestick(
            openTimestamp = Instant.parse("2019-03-05T13:01:00Z"),
            closeTimestamp = Instant.parse("2019-03-05T13:02:00Z"),
            openPrice = 9.00,
            closingPrice = 9.00,
            lowPrice = 9.00,
            highPrice = 9.00
        )

        val startsFrom = Instant.parse("2019-03-05T12:58:00Z")
        val quotes = quotes_13_00.plus(quotes_13_01)


        val result = aggregateQuotes(quotes, startsFrom, null)

        assertEquals(2, result.size)
        assertEquals(expectedCandlestick0, result[0]) // Verify candlestick of 13:00
        assertEquals(expectedCandlestick1, result[1]) // Verify candlestick of 13:01
    }
}
