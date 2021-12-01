package com.merseyside.utils

import android.os.Handler

class CancellableHandler(val handler: Handler, val runnable: Runnable) {

    fun cancel() {
        handler.removeCallbacks(runnable)
    }
}