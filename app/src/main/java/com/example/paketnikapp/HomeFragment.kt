package com.example.paketnikapp

import android.os.Bundle
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
                    val jsonObject = JSONObject(responseBody)
                    val data = jsonObject.optString("data", "")

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