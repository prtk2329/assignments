package event

import ISIN
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed class Event {
    data class InstrumentRemoved(val isin: ISIN) : Event()
}

object EventBus {
    private val flow = MutableSharedFlow<Event>(replay = 0)
    val events = flow.asSharedFlow()

    suspend fun send(event: Event) {
        flow.emit(event)
    }

    fun reset() {
        flow.resetReplayCache()
    }
}
