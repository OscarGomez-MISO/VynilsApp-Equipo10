package com.example.vynilsappequipo10.ui.albums.albumDetail

import com.example.vynilsappequipo10.data.AlbumRepository
import com.example.vynilsappequipo10.data.CollectorRepository
import com.example.vynilsappequipo10.domain.Collector
import com.example.vynilsappequipo10.ui.main.UserSession
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommentViewModelTest {

    private lateinit var viewModel: CommentViewModel
    private val albumRepository: AlbumRepository = mockk()
    private val collectorRepository: CollectorRepository = mockk()
    private val userSession: UserSession = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CommentViewModel(albumRepository, collectorRepository, userSession)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `postComment success with existing session`() = runTest {
        // Given
        val albumId = 1
        val email = "test@test.com"
        every { userSession.getCollectorId() } returns 100
        every { userSession.getCollectorEmail() } returns email
        coEvery { albumRepository.addComment(any(), any()) } returns mockk()

        // When
        viewModel.postComment(albumId, "Great album", 5, email)

        // Then
        assertTrue(viewModel.state.value is CommentUiState.Success)
        coVerify { albumRepository.addComment(albumId, match { it.rating == 5 && it.description == "Great album" }) }
    }

    @Test
    fun `postComment success finding existing collector`() = runTest {
        // Given
        val albumId = 1
        val email = "test@test.com"
        val collector = Collector(id = 200, name = "Collector", telephone = "123", email = email)
        
        every { userSession.getCollectorId() } returns -1
        coEvery { collectorRepository.getCollectors() } returns listOf(collector)
        coEvery { albumRepository.addComment(any(), any()) } returns mockk()

        // When
        viewModel.postComment(albumId, "Nice", 4, email)

        // Then
        assertTrue(viewModel.state.value is CommentUiState.Success)
        verify { userSession.saveCollector(200, email) }
    }

    @Test
    fun `postComment need profile when collector not found and no info provided`() = runTest {
        // Given
        val email = "new@test.com"
        every { userSession.getCollectorId() } returns -1
        coEvery { collectorRepository.getCollectors() } returns emptyList()

        // When
        viewModel.postComment(1, "Nice", 4, email)

        // Then
        assertTrue(viewModel.state.value is CommentUiState.NeedProfile)
        assertEquals(email, (viewModel.state.value as CommentUiState.NeedProfile).email)
    }

    @Test
    fun `postComment success creating new collector`() = runTest {
        // Given
        val email = "new@test.com"
        val collector = Collector(id = 300, name = "New User", telephone = "999", email = email)
        
        every { userSession.getCollectorId() } returns -1
        coEvery { collectorRepository.getCollectors() } returns emptyList()
        coEvery { collectorRepository.createCollector(any()) } returns collector
        coEvery { albumRepository.addComment(any(), any()) } returns mockk()

        // When
        viewModel.postComment(1, "Great", 5, email, name = "New User", telephone = "999")

        // Then
        assertTrue(viewModel.state.value is CommentUiState.Success)
        verify { userSession.saveCollector(300, email) }
        coVerify { collectorRepository.createCollector(match { it.email == email }) }
    }

    @Test
    fun `resetState changes state to Idle`() {
        // Given
        val email = "test@test.com"
        every { userSession.getCollectorId() } returns -1
        coEvery { collectorRepository.getCollectors() } returns emptyList()
        viewModel.postComment(1, "Nice", 4, email) // Set to NeedProfile

        // When
        viewModel.resetState()

        // Then
        assertEquals(CommentUiState.Idle, viewModel.state.value)
    }
}
