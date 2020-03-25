package com.merseyside.merseyLib.presentation.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialog private constructor(val dialog: AlertDialog) {

    fun show() {
        dialog.show()
    }


    class Builder(private val context: Context) {

        private var title: String? = null
        private var message: String? = null
        private var positiveButtonText: String = "Yes"
        private var negativeButtonText: String = "No"

        private var onPositiveClick: () -> Unit = {}
        private var onNegativeClick: () -> Unit = {}

        private var isOneAction: Boolean = false

        private var isCancelable: Boolean = true

        fun setTitle(title: String?): Builder {
            if (!title.isNullOrEmpty()) {
                this.title = title
            }
            return this
        }

        fun setMessage(message: String?): Builder {
            if (!message.isNullOrEmpty()) {
                this.message = message
            }

            return this
        }

        fun setPositiveButtonText(text: String?): Builder {
            if (!text.isNullOrEmpty()) {
                this.positiveButtonText = text
            }

            return this
        }

        fun setNegativeButtonText(text: String?): Builder {
            if (!text.isNullOrEmpty()) {
                this.negativeButtonText = text
            }

            return this
        }

        fun isCancelable(isCancelable: Boolean?): Builder {
            if (isCancelable != null) {
                this.isCancelable = isCancelable
            }

            return this
        }

        fun isOneAction(isOneAction: Boolean?): Builder {
            if (isOneAction != null) {
                this.isOneAction = isOneAction
            }

            return this
        }

        fun setOnPositiveClick(func: () -> Unit = {}): Builder {
            this.onPositiveClick = func

            return this
        }

        fun setOnNegativeClick(func: () -> Unit = {}): Builder {
            this.onNegativeClick = func

            return this
        }


        fun build(): MaterialAlertDialog {
            val builder = MaterialAlertDialogBuilder(context)

            builder.setMessage(message)
            builder.setCancelable(isCancelable)
            builder.setTitle(title)

            builder.setPositiveButton(
                positiveButtonText
            ) { dialog, _ ->
                onPositiveClick()

                dialog.cancel()
            }

            if (!isOneAction) {
                builder.setNegativeButton(
                    negativeButtonText
                ) { dialog, _ ->
                    onNegativeClick()

                    dialog.cancel()
                }
            }

            return MaterialAlertDialog(builder.create())
        }

    }
}