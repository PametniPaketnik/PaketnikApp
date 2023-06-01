package com.example.paketnikapp

import com.example.lib.History
import com.example.lib.Mailbox
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.IOException
import timber.log.Timber

class HttpCalls {
    companion object {
        private const val url = "http://192.168.56.1:3001/api/"
        ///private const val url = "http://192.168.1.18:3001/api/"

        suspend fun login(username: String, password: String): Boolean = withContext(Dispatchers.IO) {
            try {
                var userExists = false
                val client = OkHttpClient()

                val requestBody = FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .build()

                val loginUrl = url + "user/login"

                val request = Request.Builder()
                    .url(loginUrl)
                    .post(requestBody)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.use {
                            if (!response.isSuccessful) throw IOException("Unexected code $response")

                            userExists = true
                        }
                    }
                })

                delay(1000)
                userExists
            }
            catch (e: Exception) {
                Timber.tag("HTTP CALLS").v(e.stackTrace.toString())
                false
            }
        }

        suspend fun getHistoryById(mailboxId: String): List<History> = withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
                val historyUrl = url + "history/$mailboxId"

                val request = Request.Builder()
                    .url(historyUrl)
                    .get()
                    .build()

                val response = client.newCall(request).execute()

                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val responseBody = response.body?.string()

                val histories = Gson().fromJson(responseBody, Array<History>::class.java)

                histories.forEach { history ->
                    history.parentMailBox = mailboxId
                }

                histories.toList() // Convert the array of histories to a list and return

            } catch (e: Exception) {
                Timber.tag("HTTP CALLS").v(e.stackTraceToString())
                emptyList() // Return an empty list if an exception occurs
            }
        }

        suspend fun getMailboxById(mailboxId: String): Mailbox? = withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
                val mailboxUrl = url + "mailbox/$mailboxId"

                val request = Request.Builder()
                    .url(mailboxUrl)
                    .get()
                    .build()

                val response = client.newCall(request).execute()

                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val responseBody = response.body?.string()

                val mailbox = Gson().fromJson(responseBody, Mailbox::class.java)

                mailbox // Return the retrieved mailbox

            } catch (e: Exception) {
                Timber.tag("HTTP CALLS").v(e.stackTraceToString())
                null // Return null if an exception occurs
            }
        }

        suspend fun addHistory(parentMailBox: String, open: String): Boolean = withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()

                val requestBody = FormBody.Builder()
                    .add("parentMailBox", parentMailBox)
                    .add("open", open)
                    .build()

                val historyUrl = url + "history"

                val request = Request.Builder()
                    .url(historyUrl)
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                response.isSuccessful
            } catch (e: Exception) {
                Timber.tag("HTTP CALLS").v(e.stackTraceToString())
                false
            }
        }
    }

}