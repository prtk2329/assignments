package repository

import ISIN
import Instrument

class InstrumentRepository {

    private val instruments = mutableSetOf<ISIN>()

    fun add(instrument: Instrument) = instruments.add(instrument.isin)

    fun remove(instrument: Instrument) = instruments.remove(instrument.isin)

    fun exists(isin: ISIN): Boolean = instruments.contains(isin)
}
