package com.nextpass.nextiati.nextpass.ui.suggestion

import android.os.Bundle
import android.view.View
import com.nextpass.nextiati.nextpass.R
import com.nextpass.nextiati.nextpass.base.BaseFragment
import com.nextpass.nextiati.nextpass.databinding.FragmentSuggestionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuggestionFragment : BaseFragment<FragmentSuggestionsBinding>(R.layout.fragment_suggestions) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        with(binding) {

        }
    }

    private fun observeViewModel() {

    }
}