package com.example.compass.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compass.api.CompassRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.dsl.module

val aboutViewModelModule = module {
    factory { AboutViewModel(get()) }
}

class AboutViewModel(
    private val compassRepository: CompassRepository
) : ViewModel() {

    private val aboutChannel = Channel<AboutState>()
    val aboutFlow: Flow<AboutState>
        get() = aboutChannel.receiveAsFlow()

    fun runAboutPageRequests() {
        viewModelScope.launch {
            aboutChannel.send(AboutState.Loading)

            val aboutPage = compassRepository.getAboutPage()
            val characters = async { getEvery10thCharacter(aboutPage) }
            val wordsCount = async { getEveryWordCount(aboutPage) }

            aboutChannel.send(AboutState.Every10CharacterReceived(characters.await()))
            aboutChannel.send(AboutState.WordsCountReceived(wordsCount.await()))

            aboutChannel.send(AboutState.Idle)
        }
    }

    fun getEveryWordCount(aboutPage: String): List<String> {
        val result = ArrayList<String>()
        val wordsMap = LinkedHashMap<String, Int>()
        aboutPage.split(" ").forEach {
            val trimmed = it.trim()
            val lowerCase = trimmed.lowercase()

            if (wordsMap.containsKey(lowerCase)) {
                wordsMap[lowerCase] = wordsMap[lowerCase]!! + 1
            } else {
                wordsMap[lowerCase] = 1
            }
        }
        wordsMap.forEach {
            result.add("${it.key} : ${it.value}")
        }

        return result
    }

    fun getEvery10thCharacter(page: String): List<String> {
        val charactersList = ArrayList<String>()
        var index = 9
        while (index < page.length) {
            charactersList.add(page[index].toString())
            index += 10
        }
        return charactersList
    }
}