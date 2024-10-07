package repository

import ISIN
import Quote
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Timer
import java.util.TreeMap
import java.util.concurrent.ConcurrentHashMap
import org.slf4j.LoggerFactory
import utils.truncateToMinute
import kotlin.concurrent.schedule

class QuoteRepository(private val instrumentRepository : InstrumentRepository) {
    private val logger = LoggerFactory.getLogger("QuoteRepository")

    private val instrumentQuotes = ConcurrentHashMap<ISIN, TreeMap<Instant, MutableList<Quote>>>()
    private val retentionDuration = Duration.of(32, ChronoUnit.MINUTES)

    init {
        // Schedule a cleanup task to periodically remove expired quotes
        Timer().schedule(1000,1 * 60 * 1000) { // every minute
            cleanupExpiredQuotes()
        }
    }

    fun add(quote: Quote) {
        if (!instrumentRepository.exists(quote.isin)) {
            logger.warn("Instrument {} does not exists", quote.isin)
            return
        }

        val truncatedTimestamp = truncateToMinute(quote.timestamp)
        val quotesByTimestamp = instrumentQuotes.getOrDefault(quote.isin, TreeMap<Instant, MutableList<Quote>>())
        val quotes = quotesByTimestamp.getOrDefault(truncatedTimestamp, mutableListOf())
        quotes.add(quote)

        quotesByTimestamp[truncatedTimestamp] = quotes
        instrumentQuotes[quote.isin] = quotesByTimestamp
    }

    fun remove(isin: ISIN) = instrumentQuotes.remove(isin)

    fun getQuotesFromTimestamp(isin: ISIN, start: Instant, end: Instant): Map<Instant, List<Quote>> {
        val quotesByTimestamp = instrumentQuotes[isin] ?: return emptyMap()
        return quotesByTimestamp
            .subMap(truncateToMinute(start), true, truncateToMinute(end), true)
    }

    fun findPreviousQuote(isin: ISIN, before: Instant): Pair<Instant, List<Quote>>? {
        val quotesByTimestamp = instrumentQuotes[isin] ?: return null
        val prevTimestamp = truncateToMinute(before)
        val lastQuotes = quotesByTimestamp.headMap(prevTimestamp, false).lastEntry() ?: return null

        return Pair(lastQuotes.key, lastQuotes.value)
    }

    private fun cleanupExpiredQuotes() {
        val now = Instant.now()
        val cutoffTime = truncateToMinute(now.minus(retentionDuration))

        instrumentQuotes.values.forEach { quotesByTimestamp ->
            quotesByTimestamp.headMap(cutoffTime, false).clear()
        }
    }
}
