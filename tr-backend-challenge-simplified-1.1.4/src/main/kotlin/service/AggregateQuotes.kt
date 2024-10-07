package service

import Candlestick
import Quote
import java.time.Instant
import utils.plusOneMinute
import utils.truncateToMinute

fun aggregateQuotes(
    quotesByTimestamp: Map<Instant, List<Quote>>,
    startsFrom: Instant,
    previousQuotesByTimestamp: Pair<Instant, List<Quote>>?
): List<Candlestick> {

    if (quotesByTimestamp.isEmpty()) {
        return emptyList()
    }

    val candlesticks = mutableMapOf<Instant, Candlestick>()
    var startMinute = truncateToMinute(startsFrom)

    var prevCandlestick = if (previousQuotesByTimestamp != null) {
        getCandlestick(previousQuotesByTimestamp.first, previousQuotesByTimestamp.second)
    } else {
        null
    }

    for ((currentMinute, minuteQuotes) in quotesByTimestamp) {
        val candlestick = getCandlestick(currentMinute, minuteQuotes)

        while (startMinute.isBefore(currentMinute) && !candlesticks.containsKey(startMinute) && prevCandlestick != null) {

            candlesticks[startMinute] = prevCandlestick.copy(
                openTimestamp = startMinute,
                closeTimestamp = plusOneMinute(startMinute)
            )
            startMinute = plusOneMinute(startMinute)
        }

        startMinute = plusOneMinute(currentMinute)
        prevCandlestick = candlestick

        candlesticks[currentMinute] = candlestick
    }
    return candlesticks.values.toList()
}

private fun getCandlestick(minute: Instant, minuteQuotes: List<Quote>): Candlestick {
    val openQuote = minuteQuotes.first()
    val closingQuote = minuteQuotes.last()
    val highPrice = minuteQuotes.maxOf { it.price }
    val lowPrice = minuteQuotes.minOf { it.price }

    return Candlestick(
        openTimestamp = minute,
        closeTimestamp = plusOneMinute(minute),
        openPrice = openQuote.price,
        closingPrice = closingQuote.price,
        highPrice = highPrice,
        lowPrice = lowPrice
    )
}
