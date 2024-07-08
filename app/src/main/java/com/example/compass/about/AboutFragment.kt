package com.example.compass.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.compass.about.adapter.CharactersAdapter
import com.example.compass.databinding.FragmentAboutBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module


val aboutFragmentModule = module {
    factory { AboutFragment() }
}

class AboutFragment : Fragment() {
    private val aboutViewModel: AboutViewModel by viewModel()
    private lateinit var binding: FragmentAboutBinding
    private val charactersAdapter = CharactersAdapter()
    private val wordsCountAdapter = CharactersAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAboutBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                aboutViewModel.aboutFlow.collect { state ->
                    handleAboutState(state)
                }
            }
        }

        return binding.root
    }

    private fun handleAboutState(state: AboutState) {
        when (state) {
            is AboutState.Every10CharacterReceived -> onEvery10CharacterReceived(state)
            is AboutState.Idle -> hideLoader()
            is AboutState.Loading -> showLoader()
            is AboutState.WordsCountReceived -> onWordsCountReceived(state)
        }
    }

    private fun onWordsCountReceived(state: AboutState.WordsCountReceived) {
        wordsCountAdapter.updateList(state.wordsCount, clearList = true)
    }

    private fun onEvery10CharacterReceived(state: AboutState.Every10CharacterReceived) {
        charactersAdapter.updateList(state.characters, clearList = true)
    }

    private fun showLoader() {
        binding.progressIndicator.isVisible = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
            charactersList.addItemDecoration(dividerItemDecoration)

            charactersList.adapter = charactersAdapter
            wordCounterList.adapter = wordsCountAdapter
            runButton.setOnClickListener { onRunClicked() }
        }
    }

    private fun onRunClicked() {
        aboutViewModel.runAboutPageRequests()
    }

    private fun hideLoader() {
        binding.progressIndicator.isVisible = false
    }

}