package processor

import Instrument
import InstrumentEvent
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import repository.InstrumentRepository

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class InstrumentEventProcessorTest(@MockK private val repository: InstrumentRepository) {

    private lateinit var processor: InstrumentEventProcessor

    private val addInstrumentEvent = InstrumentEvent(
        type = InstrumentEvent.Type.ADD,
        data = Instrument("XYZ1234", "Add Instrument Event")
    )

    private val deleteInstrumentEvent = InstrumentEvent(
        type = InstrumentEvent.Type.DELETE,
        data = Instrument("XYZ1234", "Delete Instrument Event")
    )

    @BeforeEach
    fun setup() {
        processor = InstrumentEventProcessor(repository)

        every { repository.add(addInstrumentEvent.data) } returns true
        every { repository.remove(deleteInstrumentEvent.data) } returns true
    }

    @Test
    fun testShouldProcessAddInstrumentEvent() = runTest {
        processor.process(addInstrumentEvent)

        verify(exactly = 1) { repository.add(addInstrumentEvent.data) }
    }

    @Test
    fun testShouldProcessDeleteInstrumentEvent() = runTest {
        processor.process(deleteInstrumentEvent)
        verify(exactly = 1) { repository.remove(deleteInstrumentEvent.data) }
    }
}
