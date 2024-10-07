package service.impl

import Candlestick
import Quote
import java.time.Duration
import java.time.Instant
import service.CandlestickManager
import service.aggregateQuotes
import repository.QuoteRepository
import utils.truncateToMinute

class CandleStickManagerImpl(private val repository: QuoteRepository) : CandlestickManager {

    override fun getCandlesticks(isin: String): List<Candlestick> {
        val duration = Duration.ofMinutes(30)
        val end = Instant.now()
        val start = end.minus(duration)

        val quotesByTimestamp = repository.getQuotesFromTimestamp(isin, start, end)

        if (quotesByTimestamp.isEmpty()) {
            return emptyList()
        }

        var previousQuotesByTimestamp: Pair<Instant, List<Quote>>? = null

        if (!hasQuotesPresentForStartingMinute(quotesByTimestamp, start)) {
            previousQuotesByTimestamp = repository.findPreviousQuote(isin, start)
        }
        return aggregateQuotes(quotesByTimestamp, start, previousQuotesByTimestamp)
    }

    private fun hasQuotesPresentForStartingMinute(
        quotesByTimestamp: Map<Instant, List<Quote>>,
        start: Instant
    ): Boolean {
        val startingMinute = truncateToMinute(start)
        return quotesByTimestamp.containsKey(startingMinute)
    }
}
