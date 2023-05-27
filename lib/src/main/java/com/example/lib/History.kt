package com.example.lib

import java.util.Date

class History(
    val date: Date,
    val parentMailbox: String,
    val open: String
) {
    override fun toString(): String {
        return "Histories : $date, $parentMailbox, $open"
    }
}