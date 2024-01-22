package com.kylix.model

data class EmailRequest(
    val to: String,
    val subject: String,
    val body: String
)