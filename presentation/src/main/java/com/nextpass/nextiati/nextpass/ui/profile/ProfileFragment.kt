package com.nextpass.nextiati.nextpass.ui.profile

import android.os.Bundle
import android.view.View
import com.nextpass.nextiati.nextpass.R
import com.nextpass.nextiati.nextpass.base.BaseFragment
import com.nextpass.nextiati.nextpass.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
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