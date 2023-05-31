package com.example.lib

import java.util.Date

class Mailbox(
    val boxID: String,
    val street: String,
    val postcode: Number,
    val post: String,
    val country: String,
    val lat: Number,
    val lng: Number,
    val open: Boolean,
    val date: Date,
    /*val userId: User,
    val mailboxUser: User,
    val accessUser: List<User>*/

)
{
    override fun toString(): String {
        return "Mailbox id: $boxID"
    }

}