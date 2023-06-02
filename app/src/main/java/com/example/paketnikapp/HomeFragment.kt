package com.example.paketnikapp

import android.app.AlertDialog
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.paketnikapp.HttpCalls.Companion.addHistory
import com.example.paketnikapp.databinding.FragmentHomeBinding
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var audioManager: AudioManager
    private lateinit var audioFocusRequest: AudioFocusRequest
    private lateinit var audioFocusChangeListener: AudioManager.OnAudioFocusChangeListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requestAudioFocus()
        mediaPlayer?.start()
    }

    override fun onPause() {
        super.onPause()
        releaseAudioFocus()
        mediaPlayer?.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mailboxFragmentButton()

        audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> {
                    mediaPlayer?.start()
                }
                AudioManager.AUDIOFOCUS_LOSS -> {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = null
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    mediaPlayer?.pause()
                }
            }
        }

        val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
            val parts = result.toString().split("/")
            val id = parts[4].removePrefix("0").toIntOrNull()

            if (id != null) {
                // Toast.makeText(activity, "ID extracted from QR code: $id", Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val mailbox = HttpCalls.getMailboxById(id.toString())
                        if (mailbox != null) {
                            DataHolder.parentMailBox = mailbox._id
                            DataHolder.lng = mailbox.lng
                            DataHolder.lat = mailbox.lat
                            DataHolder.street = mailbox.street
                            DataHolder.postcode = mailbox.postcode

                            Toast.makeText(context, "Playing token for the box with id $id", Toast.LENGTH_SHORT).show()

                            delay(6000)
                            showConfirmationDialog(mailbox._id)
                        } else {
                            Toast.makeText(context, "Error retrieving mailbox", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error retrieving mailbox", Toast.LENGTH_SHORT).show()
                    }
                }

                DataHolder.id = id.toString()
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
                    val decoded64data = Base64.decode(data, Base64.DEFAULT)
                    playAudioFromByteArray(decoded64data)

                    /*activity?.runOnUiThread {
                        Toast.makeText(activity, "Data: $data", Toast.LENGTH_SHORT).show()
                    }*/
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

    private fun playAudioFromByteArray(data: ByteArray) {
        try {
            val tempFile = createTempFile("temp", ".mp3") // Create a temporary file to store the audio data

            // Write the audio data to the temporary file
            tempFile.writeBytes(data)

            mediaPlayer = MediaPlayer()

            // Set the data source for the media player
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mediaPlayer!!.setDataSource(tempFile.toFile().absolutePath)
            }

            // Prepare the media player asynchronously
            mediaPlayer!!.prepareAsync()

            // Set a listener to start playing the audio once the media player is prepared
            mediaPlayer!!.setOnPreparedListener {
                mediaPlayer?.start()
            }

            // Set a listener to release the media player resources after playback is complete
            mediaPlayer?.setOnErrorListener { _, _, _ ->
                mediaPlayer?.release()
                mediaPlayer = null // Set mediaPlayer to null after releasing
                tempFile.deleteExisting()
                true
            }
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun sendPostRequest(url: String, jsonBody: String, token: String, callback: Callback) {
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

    private fun requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build())
                .setOnAudioFocusChangeListener(audioFocusChangeListener)
                .build()

            audioFocusRequest = focusRequest
            audioManager.requestAudioFocus(focusRequest)
        }
        else {
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(
                audioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }
    }

    private fun releaseAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(audioFocusRequest)
        }
        else {
            @Suppress("DEPRECATION")
            audioManager.abandonAudioFocus(audioFocusChangeListener)
        }
    }

    private fun mailboxFragmentButton() {
        binding.buttonMailbox.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToMailboxFragment()
            findNavController().navigate(action)
        }
    }


    private fun showConfirmationDialog(id: String) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Confirmation")
            .setMessage("Do you successfully open the box?")
            .setPositiveButton("Yes") { _, _ ->
                val open = "Successful"

                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val success = addHistory(id, open)
                        if (success) {
                            Toast.makeText(context, "Successfully added history", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error adding history", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No") { _, _ ->
                val open = "Unsuccessful"
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val success = addHistory(id, open)
                        if (success) {
                            Toast.makeText(context, "Successfully added history", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error adding history", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .create()

        dialog.show()
    }
}