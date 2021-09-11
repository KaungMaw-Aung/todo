package com.kaungmaw.todo.util

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kaungmaw.todo.databinding.DialogProgressLoadingBinding

class LoadingDialog {

    companion object {
        private lateinit var dialog: AlertDialog

        fun show(context: Context) {
            val builder = MaterialAlertDialogBuilder(context)
            val inflater: LayoutInflater = LayoutInflater.from(builder.context)
            val dialogBinding = DialogProgressLoadingBinding.inflate(
                inflater, ConstraintLayout(builder.context), false
            )
            builder.setView(dialogBinding.root)
            dialog = builder.create()
            dialog.show()
        }

        fun dismiss() {
            dialog.dismiss()
        }
    }
}