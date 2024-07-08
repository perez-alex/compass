package com.example.compass.about

import com.example.compass.api.CompassRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import io.mockk.coEvery
import io.mockk.mockk

@OptIn(ExperimentalCoroutinesApi::class)
class AboutViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: AboutViewModel
    private lateinit var compassRepository: CompassRepository

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        compassRepository = mockk {
            coEvery { getAboutPage() } returns "This is a test string for the about page."
        }
        viewModel = AboutViewModel(compassRepository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldEmitLoadingStateBeforeFetchingData() = testScope.runTest {
        viewModel.runAboutPageRequests()
        val state = viewModel.aboutFlow.first()
        assertEquals(AboutState.Loading, state)
    }

    @Test
    fun shouldEmitEvery10CharacterReceivedStateWithCorrectCharacters() =
        testScope.runTest {
            viewModel.runAboutPageRequests()
            // Skip the Loading state
            viewModel.aboutFlow.first()
            val every10thCharacterState = viewModel.aboutFlow.first {
                it is AboutState.Every10CharacterReceived
            } as AboutState.Every10CharacterReceived
            assertEquals(listOf(" ", "n", " ", "e"), every10thCharacterState.characters)
        }

    @Test
    fun shouldEmitWordsCountReceivedStateWithCorrectCounts() =
        testScope.runTest {
            viewModel.runAboutPageRequests()
            // Skip the Loading and Every10CharacterReceived states
            repeat(2) { viewModel.aboutFlow.first() }
            val wordsCountState = viewModel.aboutFlow.first {
                it is AboutState.WordsCountReceived
            } as AboutState.WordsCountReceived
            val expectedCounts = listOf(
                "this : 1",
                "is : 1",
                "a : 1",
                "test : 1",
                "string : 1",
                "for : 1",
                "the : 1",
                "about : 1",
                "page. : 1"
            )
            assertEquals(expectedCounts, wordsCountState.wordsCount)
        }

    @Test
    fun shouldEmitIdleStateAfterFetchingData() = testScope.runTest {
        viewModel.runAboutPageRequests()
        // Advance the flow to the Idle state
        repeat(3) { viewModel.aboutFlow.first() }
        val idleState = viewModel.aboutFlow.first()
        assertEquals(AboutState.Idle, idleState)
    }

    @Test
    fun getEveryWordCountReturnsCorrectCounts() {
        val testString = "This is a test string for the about page."
        val expectedCounts = listOf(
            "this : 1",
            "is : 1",
            "a : 1",
            "test : 1",
            "string : 1",
            "for : 1",
            "the : 1",
            "about : 1",
            "page. : 1"
        )
        val result = viewModel.getEveryWordCount(testString)
        assertEquals(expectedCounts, result)
    }

    @Test
    fun getEvery10thCharacterReturnsCorrectCharacters() {
        val testString = "This is a test string for the about page."
        val expectedCharacters = listOf(" ", "n", " ", "e")
        val result = viewModel.getEvery10thCharacter(testString)
        assertEquals(expectedCharacters, result)
    }
}