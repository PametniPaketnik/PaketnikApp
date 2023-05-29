package com.example.paketnikapp

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.paketnikapp.databinding.FragmentHomeBinding
import io.github.g00fy2.quickie.ScanQRCode
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import kotlin.io.path.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
            val parts = result.toString().split("/")
            val id = parts[4].removePrefix("0").toIntOrNull()

            if (id != null) {
                Toast.makeText(activity, "ID extracted from QR code: $id", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Failed to extract ID from QR code", Toast.LENGTH_SHORT).show()
            }

            val url = "https://api-d4me-stage.direct4.me/sandbox/v1/Access/openbox"
            val requestBodyJson = """
                {
                    "boxId": $id,
                    "tokenFormat": 5
                }
            """.trimIndent()

            val bearerToken = "9ea96945-3a37-4638-a5d4-22e89fbc998f"

            sendPostRequest(url, requestBodyJson, bearerToken, object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()

                    // Parse the JSON response
                    val jsonObject = JSONObject(responseBody!!)
                    val data = jsonObject.optString("data", "")

                    // Decode the received data to a bytearray
                    // Parameters:
                    // 1. str = String!: the input String to decode, which is converted to bytes using the default charset
                    // 2. flags = Int: controls certain features of the decoded output. Pass DEFAULT to decode standard Base64.
                    val decoded64Data = Base64.decode(data, 0)
                    playAudioFromByteArray(decoded64Data)

                    activity?.runOnUiThread {
                        Toast.makeText(activity, "Data: $data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    activity?.runOnUiThread {
                        Toast.makeText(activity, "Request failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        binding.buttonScanQRCode.setOnClickListener {
            scanQrCodeLauncher.launch(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun playAudioFromByteArray(data: ByteArray) {
        try {
            val tempFile = createTempFile("temp", ".mp3") // Create a temporary file to store the audio data

            // Write the audio data to the temporary file
            tempFile.writeBytes(data)

            val mediaPlayer = MediaPlayer()

            // Set the data source for the media player
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mediaPlayer.setDataSource(tempFile.toFile().absolutePath)
            }

            // Prepare the media player asynchronously
            mediaPlayer.prepareAsync()

            // Set a listener to start playing the audio once the media player is prepared
            mediaPlayer.setOnPreparedListener {
                it.start()
            }

            // Set a listener to release the media player resources after playback is complete
            mediaPlayer.setOnCompletionListener {
                it.release()
                tempFile.deleteIfExists()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun sendPostRequest(url: String, jsonBody: String, token: String, callback: Callback) {
    val client = OkHttpClient()
    val mediaType = "application/json".toMediaTypeOrNull()
    val requestBody = jsonBody.toRequestBody(mediaType)
    val headers = Headers.Builder()
        .add("Authorization", "Bearer $token")
        .build()

    val request = Request.Builder()
        .url(url)
        .headers(headers)
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(callback)
}