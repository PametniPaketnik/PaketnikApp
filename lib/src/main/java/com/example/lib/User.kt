package com.example.lib

class User(
    val username: String,
    val password: String,
    val email: String,
    val isAdmin: Boolean,
    val firstName: String,
    val lastName: String,
    val tel: String,
    val street: String,
    val postcode: String,
    val path: String
) {
    override fun toString(): String {
        return "User: $username"
    }
}