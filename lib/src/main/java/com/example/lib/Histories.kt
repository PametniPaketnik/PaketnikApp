package com.example.lib

import kotlin.random.Random

class Histories(private val historyList: MutableList<History> = mutableListOf()) {
    companion object {
        fun generate(num: Int): Histories {
            val historyList: MutableList<History> = mutableListOf()

            for (i in 0 until num) {
                val isSuccess = Random.nextBoolean()
                val open = if (isSuccess) "Successful" else "Unsuccessful"
                historyList.add(History("1111", open))
            }

            return Histories(historyList)
        }
    }

    fun getHistoryList(): MutableList<History> {
        return historyList
    }
}