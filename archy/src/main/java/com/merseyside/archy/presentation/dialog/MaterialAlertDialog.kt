package com.merseyside.archy.presentation.dialog

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

        private var isSingleAction: Boolean = false

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

        fun isSingleAction(isSingleAction: Boolean?): Builder {
            if (isSingleAction != null) {
                this.isSingleAction = isSingleAction
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

            builder.apply {
                setMessage(message)
                setCancelable(isCancelable)
                setTitle(title)

                setPositiveButton(
                    positiveButtonText
                ) { dialog, _ ->
                    onPositiveClick()

                    dialog.cancel()
                }

                if (!isSingleAction) {
                    setNegativeButton(negativeButtonText) { dialog, _ ->
                        onNegativeClick()
                        dialog.cancel()
                    }
                }
            }

            return MaterialAlertDialog(builder.create())
        }

    }
}