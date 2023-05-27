package com.example.lib

import java.util.Date

fun main() {

    val usr = User("Sinisa", "1234", "sinisa.vucetic@gmail.com", true, "Sinisa", "Vucetic", "0393048594", "Ulica 4", "2000", "pot")
    val accessUsers = listOf(usr,usr)
    val mailbox = Mailbox("404", "Proletarskih brigad 59", 2000, "Maribor", "Slovenija", 46.5594322, 15.6401118, false, Date(), usr, usr,accessUsers )
    val histories1 = History(Date(), "404", "ok")
    println(usr.toString())
    println(mailbox.toString())
    println(histories1.toString())
}