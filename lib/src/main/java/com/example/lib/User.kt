package com.example.lib

data class User(
    val username: String, val password: String, val email: String,
    val isAdmin: Boolean, val firstName: String, val lastName: String,
    val tel: String, val street: String, val postcode: String,
    val lat: Double, val lng: Double
) {
    constructor() : this(
        "", "", "",
        false, "", "",
        "", "", "", 0.0, 0.0
    )
}