package com.merseyside.merseyLib.features.adapters.concat.entity

data class News private constructor(
    val id: Int,
    val title: String,
    val description: String
) {
    constructor(
        id: Int
    ): this(id, "News title $id", "News description $id")
}
