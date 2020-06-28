package com.merseyside.animators

fun AnimatorList?.isNotNullAndEmpty(): Boolean {
    return this != null && isNotEmpty()
}