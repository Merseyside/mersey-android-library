package com.merseyside.archy.presentation.view.localeViews

import android.content.Context
import androidx.annotation.StringRes

data class LocaleData(
    @StringRes val id: Int,
    val args: Array<String> = emptyArray(),
    val context: Context? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocaleData

        if (id != other.id) return false
        return args.contentEquals(other.args)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + args.contentHashCode()
        return result
    }
}