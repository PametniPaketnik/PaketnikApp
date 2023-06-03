package com.example.paketnikapp

import android.hardware.Camera
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.paketnikapp.databinding.FragmentLoginCameraBinding

class LoginCameraFragment : Fragment() {
    private var _binding: FragmentLoginCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var camera: Camera
    private var cameraId: Int = -1

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

        // Set up camera and surface holder
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    // Find the front-facing camera
                    val numberOfCameras = Camera.getNumberOfCameras()
                    for (i in 0 until numberOfCameras) {
                        val cameraInfo = Camera.CameraInfo()
                        Camera.getCameraInfo(i, cameraInfo)
                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                            cameraId = i
                            break
                        }
                    }

                    // Open the front-facing camera
                    camera = Camera.open(cameraId)
                    camera.setPreviewDisplay(holder)
                    camera.startPreview()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error opening camera: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                // Handle surface changes if needed
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                camera.stopPreview()
                camera.release()
            }
        })

        // Button click listener to stop the camera
        btnStop.setOnClickListener {
            camera.stopPreview()
            camera.release()
            // Toast.makeText(requireContext(), "Camera stopped", Toast.LENGTH_SHORT).show()

            val action = LoginCameraFragmentDirections.actionLoginCameraFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

