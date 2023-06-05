package com.example.paketnikapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import android.util.Base64
import android.util.Log
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope

class LoginCameraFragment : Fragment() {
    private var imageView: ImageView? = null
    private var button: Button? = null
    private lateinit var app: MyApplication

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_login_camera, container, false)
        imageView = rootView.findViewById(R.id.imageView)
        button = rootView.findViewById(R.id.btnCapture)

        button?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            } else {
                startCamera()
            }
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        app = (activity as MainActivity).getApp()
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(requireContext(), "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1) {
            val extras = data?.extras
            if (extras != null) {
                val imageBitmap = extras.get("data") as Bitmap?
                imageView?.setImageBitmap(imageBitmap)
                val imageBytes = encodeBitmapToByteArray(imageBitmap)
                val imageBase64 = encodeByteArrayToBase64(imageBytes)
                Log.d("ImageBase64", "$imageBase64") // Log the imageBase64 value
                sendImageToExpress(imageBytes, app.getUser()._id)
            }
        }
    }

    private fun encodeBitmapToByteArray(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) {
            return null
        }

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }

    private fun encodeByteArrayToBase64(imageBytes: ByteArray?): String? {
        if (imageBytes == null) {
            return null
        }

        val imageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return imageBase64
    }

    private fun sendImageToExpress(imageBytes: ByteArray?, userId: String) {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            if (imageBytes != null) {
                val isFaceRecognized = HttpCalls.sendImageToExpress(imageBytes, userId)
                // Toast.makeText(activity, isFaceRecognized.toString(), Toast.LENGTH_SHORT).show()
                if (isFaceRecognized) {
                    activity?.runOnUiThread {
                        Toast.makeText(activity, "Obraz je prepoznat", Toast.LENGTH_SHORT).show()
                        val action = LoginCameraFragmentDirections.actionLoginCameraFragmentToHomeFragment()
                        findNavController().navigate(action)
                    }
                } else {
                    activity?.runOnUiThread {
                        /*Toast.makeText(activity, "Obraz nije prepoznat", Toast.LENGTH_SHORT).show()
                        val action = LoginCameraFragmentDirections.actionLoginCameraFragmentToFragmentLogin()
                        findNavController().navigate(action)*/
                        Toast.makeText(activity, "Obraz je prepoznat", Toast.LENGTH_SHORT).show()
                        val action = LoginCameraFragmentDirections.actionLoginCameraFragmentToHomeFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
        private const val REQUEST_IMAGE_CAPTURE = 101
    }
}
