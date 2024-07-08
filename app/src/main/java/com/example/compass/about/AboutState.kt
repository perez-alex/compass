package com.example.compass.about

sealed class AboutState {
    data object Loading : AboutState()
    data class Every10CharacterReceived(val characters: List<String>) : AboutState()
    data class WordsCountReceived(val wordsCount: List<String>) : AboutState()
    data object Idle : AboutState()
}