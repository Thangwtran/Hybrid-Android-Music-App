package com.example.hybridmusicapp.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.hybridmusicapp.R

class NetworkDialogFragment(
    private val titleId: Int,
    private val messageId: Int
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(titleId)
            .setMessage(messageId)
            .setPositiveButton(R.string.ok) { _: DialogInterface?, _: Int -> dismiss() }
            .create()
    }
}