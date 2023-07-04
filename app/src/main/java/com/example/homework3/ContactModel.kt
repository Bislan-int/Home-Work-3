package com.example.homework3

data class ContactModel(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val isEnabled: Boolean,
    var id: Int = -1
)
