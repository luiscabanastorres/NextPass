package com.nextpass.nextiati.nextpass.base

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.nextpass.nextiati.nextpass.R

abstract class BaseFragment<VDB : ViewDataBinding>(
    @LayoutRes protected val layoutId: Int,
) : Fragment() {

    protected lateinit var binding: VDB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    protected fun showMessage(title: String, message: String, onClick: () -> Unit) {

        showMessage1(
            title = title,
            message = message,
            onClick = onClick
        )
    }

    protected fun showMessage1(
        title: String,
        message: String,
        positiveTitle: Int = R.string.ok,
        onClick: () -> Unit = { }
    ) {
        createAlertDialog(title, message, positiveTitle, onClick)
    }

    private fun createAlertDialog(
        title: String,
        message: String,
        positiveTitle: Int,
        onPositiveClick: () -> Unit,
    ) {
        val builder = AlertDialog.Builder(requireActivity())
        builder
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(positiveTitle) { _, _ ->
                onPositiveClick()
            }.create().show()
    }

}
