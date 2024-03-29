package fr.pitdev.log

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import timber.log.Timber

@ExtendWith(MockKExtension::class)
class DefaultLogProviderTest {

    @InjectMockKs
    private lateinit var defaultLogProvider: DefaultLogProvider

    @RelaxedMockK
    private lateinit var debugTree: DefaultDebugTree

    @BeforeEach
    fun setUp() {
        mockkObject(Timber)
    }

    @Test
    fun `should create loggers`() {
        defaultLogProvider.create(false)
        verify {
            Timber.plant(any())
        }

    }

    @Test
    fun `should up root loggers`() {
        defaultLogProvider.terminate()
        verify {
            Timber.uprootAll()
        }

    }

}
