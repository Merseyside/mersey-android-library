package com.merseyside.adapters.feature.style

interface StyleableItem<Style : ComposingStyle> {

    var style: Style.() -> Unit
    val composingStyle: Style
}