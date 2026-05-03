package com.example.vynilsappequipo10.ui.collectors.collectorDetail

import com.example.vynilsappequipo10.data.CollectorRepository
import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.domain.CollectorAlbumWithAlbum
import com.example.vynilsappequipo10.domain.CollectorDetail
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CollectorDetailViewModelTest {

    private lateinit var viewModel: CollectorDetailViewModel
    private val repository: CollectorRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    private val fakeCollector = CollectorDetail(
        id = 1,
        name = "Manolo Bellon",
        telephone = "3502457896",
        email = "manollo@caracol.com.co"
    )

    private val fakeAlbum = Album(
        id = 1,
        name = "Buscando América",
        cover = "https://cover.url",
        releaseDate = "1984-08-01T00:00:00.000Z",
        description = "Álbum de prueba",
        genre = "Salsa",
        recordLabel = "Elektra"
    )

    private val fakeCollectorAlbums = listOf(
        CollectorAlbumWithAlbum(id = 1, price = 35.0, status = "Active", album = fakeAlbum)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCollector success updates collector and albums in uiState`() {
        coEvery { repository.getCollectorById(1) } returns fakeCollector
        coEvery { repository.getCollectorAlbums(1) } returns fakeCollectorAlbums

        viewModel = CollectorDetailViewModel(repository)
        viewModel.loadCollector(1)

        val state = viewModel.uiState.value
        assertNotNull(state.collector)
        assertEquals("Manolo Bellon", state.collector!!.name)
        assertEquals(1, state.collectorAlbums.size)
        assertEquals("Buscando América", state.collectorAlbums[0].album.name)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `loadCollector error sets error in uiState`() {
        val errorMessage = "No se pudo cargar el coleccionista"
        coEvery { repository.getCollectorById(1) } throws Exception(errorMessage)
        coEvery { repository.getCollectorAlbums(1) } returns emptyList()

        viewModel = CollectorDetailViewModel(repository)
        viewModel.loadCollector(1)

        val state = viewModel.uiState.value
        assertNull(state.collector)
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `loadCollector with empty albums shows collector with empty collection`() {
        coEvery { repository.getCollectorById(1) } returns fakeCollector
        coEvery { repository.getCollectorAlbums(1) } returns emptyList()

        viewModel = CollectorDetailViewModel(repository)
        viewModel.loadCollector(1)

        val state = viewModel.uiState.value
        assertNotNull(state.collector)
        assertTrue(state.collectorAlbums.isEmpty())
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `loadCollector exposes collector name email and telephone`() {
        coEvery { repository.getCollectorById(1) } returns fakeCollector
        coEvery { repository.getCollectorAlbums(1) } returns emptyList()

        viewModel = CollectorDetailViewModel(repository)
        viewModel.loadCollector(1)

        val collector = viewModel.uiState.value.collector!!
        assertEquals("Manolo Bellon", collector.name)
        assertEquals("manollo@caracol.com.co", collector.email)
        assertEquals("3502457896", collector.telephone)
    }
}
