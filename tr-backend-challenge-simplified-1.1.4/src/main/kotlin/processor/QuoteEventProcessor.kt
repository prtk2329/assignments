package processor

import QuoteEvent
import event.Event
import event.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repository.QuoteRepository

class QuoteEventProcessor(private val repository: QuoteRepository) {
    init {
        CoroutineScope(Dispatchers.Default).launch {
            EventBus.events.collect { event ->
                when (event) {
                    is Event.InstrumentRemoved -> repository.remove(event.isin)
                }
            }
        }
    }

    fun process(event: QuoteEvent) = repository.add(event.data)
}
