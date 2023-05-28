package com.example.paketnikapp

import android.app.Application
import com.example.lib.Histories

class MyApplication: Application() {
    private lateinit var data: Histories

    override fun onCreate() {
        super.onCreate()
        data = getFakerHistories()
    }

    private fun getFakerHistories(): Histories {
        return Histories.generate(10)
    }

    fun getHistories(): Histories {
        return data
    }
}