package com.example.paketnikapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import timber.log.Timber

class HttpCalls {
    companion object {
        private const val url = "http://192.168.56.1:3001/api/"

        suspend fun login(username: String, password: String): Boolean = withContext(Dispatchers.IO) {
            try {
                var userExists = false
                val client = OkHttpClient()
                val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

                val jsonBody = """
                {
                    "username": $username,
                    "password": $password
                }
                """.trimIndent()

                val requestBody = jsonBody.toRequestBody(mediaType)
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

                userExists
            }
            catch (e: Exception) {
                Timber.tag("HTTP CALLS").v(e.stackTrace.toString())
                false
            }
        }
    }

}