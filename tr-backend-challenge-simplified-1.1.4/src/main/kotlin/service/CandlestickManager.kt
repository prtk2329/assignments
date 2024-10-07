package service

import Candlestick

interface CandlestickManager {
    fun getCandlesticks(isin: String): List<Candlestick>
}
