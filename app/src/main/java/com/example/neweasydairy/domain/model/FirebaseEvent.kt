package com.example.neweasydairy.domain.model

data class FirebaseEvent(
    val name: String,
    val params: Map<String, Any> = emptyMap()
)