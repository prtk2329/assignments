package processor

import InstrumentEvent
import event.Event
import event.EventBus
import repository.InstrumentRepository

class InstrumentEventProcessor(private val repository: InstrumentRepository) {

    suspend fun process(event: InstrumentEvent) {
        when (event.type) {
            InstrumentEvent.Type.ADD -> repository.add(event.data)
            InstrumentEvent.Type.DELETE -> {
                repository.remove(event.data)
                EventBus.send(Event.InstrumentRemoved(event.data.isin))
            }
        }
    }
}
