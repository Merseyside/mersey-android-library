package com.merseyside.adapters.compose.style

interface StyleableItem<Style : ComposingStyle> {

    var style: Style.() -> Unit
    val composingStyle: Style
}