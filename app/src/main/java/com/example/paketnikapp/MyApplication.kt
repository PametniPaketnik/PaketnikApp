package com.example.paketnikapp

import android.app.Application
import com.example.lib.User
import com.google.gson.Gson
import timber.log.Timber
import java.io.File
import java.io.IOException
import org.apache.commons.io.FileUtils

class MyApplication: Application() {
    private lateinit var user: User
    private lateinit var gson: Gson
    private lateinit var file: File
    private val userFile = "mydata.json"

    override fun onCreate() {
        super.onCreate()
        //Timber logs
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        gson = Gson()
        file = File(filesDir, userFile)

        initUserData()
        saveUserFile()
    }

    fun saveUserFile() {
        try {
            FileUtils.writeStringToFile(file, gson.toJson(user))
        }
        catch (e: IOException) {
            Timber.d("Can't save " + file.path)
        }
    }
    private fun initUserData() {
        user = try {
            Timber.d("My file data:${FileUtils.readFileToString(file)}")
            gson.fromJson(FileUtils.readFileToString(file), User::class.java)
        }
        catch (e: IOException) {
            Timber.d("No file init data.")
            User()
        }
    }

    fun deleteUserFile() {
        try {
            FileUtils.forceDelete(file)
        }
        catch (e: IOException) {
            Timber.d("Can't save " + file.path)
        }
    }

    fun getUser(): User {
        return user
    }

    fun setUser(user: User) {
        this.user = user
    }
}