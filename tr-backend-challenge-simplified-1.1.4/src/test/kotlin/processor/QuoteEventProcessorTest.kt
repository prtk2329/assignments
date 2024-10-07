package processor

import Quote
import QuoteEvent
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import repository.QuoteRepository

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class QuoteEventProcessorTest(@MockK private val repository: QuoteRepository) {

    private lateinit var processor: QuoteEventProcessor

    private val quoteEvent = QuoteEvent(data = Quote("XYZ1234", 9.90))

    @BeforeEach
    fun setup() {
        processor = QuoteEventProcessor(repository)

        every { repository.add(quoteEvent.data) } returns Unit
    }

    @Test
    fun testShouldProcessQuoteEvent() {
        processor.process(quoteEvent)
        verify(exactly = 1) { repository.add(quoteEvent.data) }
    }
}
