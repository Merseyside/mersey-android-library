package com.merseyside.merseyLib

fun AnimatorList?.isNotNullAndEmpty(): Boolean {
    return this != null && isNotEmpty()
}