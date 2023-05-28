package com.example.lib

import java.util.Date

class History(
    val parentMailbox: String,
    val open: String,
    val date: String = "date 2023"
) {
    override fun toString(): String {
        return "Histories : $date, $parentMailbox, $open"
    }
}