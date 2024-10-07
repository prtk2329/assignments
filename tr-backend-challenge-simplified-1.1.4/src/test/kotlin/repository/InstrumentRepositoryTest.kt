package repository

import Instrument
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class InstrumentRepositoryTest {
    private lateinit var repository: InstrumentRepository

    @BeforeEach
    fun setup() {
        repository = InstrumentRepository()
    }

    @Test
    fun testShouldAddInstrument() {
        repository.add(Instrument("XYZ1234", "Add Instrument"))
        assertTrue { repository.exists("XYZ1234") }
    }

    @Test
    fun testShouldRemoveInstrument() {
        repository.remove(Instrument("XYZ1234", "Add Instrument"))
        assertFalse { repository.exists("XYZ1234") }
    }
}
