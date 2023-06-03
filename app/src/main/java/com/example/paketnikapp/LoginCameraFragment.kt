package com.example.paketnikapp

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.paketnikapp.databinding.FragmentLoginCameraBinding

class LoginCameraFragment : Fragment(), SurfaceHolder.Callback {
    private var _binding: FragmentLoginCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraDevice: CameraDevice
    private lateinit var captureSession: CameraCaptureSession
    private var cameraId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val surfaceView: SurfaceView = binding.surfaceView
        val btnStop: Button = binding.btnStop

        surfaceView.holder.addCallback(this)

        btnStop.setOnClickListener {
            stopCamera()

            val action = LoginCameraFragmentDirections.actionLoginCameraFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        cameraManager = requireContext().getSystemService(CameraManager::class.java)
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            try {
                // Find the front-facing camera
                val cameraIds = cameraManager.cameraIdList
                for (id in cameraIds) {
                    val cameraCharacteristics = cameraManager.getCameraCharacteristics(id)
                    if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                        cameraId = id
                        break
                    }
                }

                // Open the front-facing camera
                cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                    override fun onOpened(camera: CameraDevice) {
                        cameraDevice = camera
                        startPreview(holder.surface)
                    }

                    override fun onDisconnected(camera: CameraDevice) {
                        camera.close()
                    }

                    override fun onError(camera: CameraDevice, error: Int) {
                        Toast.makeText(requireContext(), "Error opening camera", Toast.LENGTH_SHORT).show()
                    }
                }, null)
            } catch (e: CameraAccessException) {
                Toast.makeText(requireContext(), "Error accessing camera", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestCameraPermission()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Handle surface changes if needed
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopCamera()
    }

    private fun startPreview(surface: Surface) {
        try {
            val surfaces = listOf(surface)
            cameraDevice.createCaptureSession(surfaces, object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    captureSession = session
                    val previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                    previewRequestBuilder.addTarget(surface)

                    // Set the rotation of the preview
                    previewRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, 90)

                    session.setRepeatingRequest(previewRequestBuilder.build(), null, null)
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    Toast.makeText(requireContext(), "Failed to configure camera preview", Toast.LENGTH_SHORT).show()
                }
            }, null)
        } catch (e: CameraAccessException) {
            Toast.makeText(requireContext(), "Error accessing camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopCamera() {
        captureSession.close()
        cameraDevice.close()
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1
    }
}


