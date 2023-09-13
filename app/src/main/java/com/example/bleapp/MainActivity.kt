package com.example.bleapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val scan: Button = findViewById<Button>(R.id.button)
        scan.setOnClickListener {
            val bleManager: BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val ble = bleManager.adapter

            if (ble == null) {
                // nie ma ble
                Toast.makeText(getApplicationContext(), "Brak interfejsu Bluetooth!", Toast.LENGTH_LONG).show()
            } else {
                // jest ble
                if (ble.isEnabled) {
                    Toast.makeText(getApplicationContext(), "ble jest włączone", Toast.LENGTH_LONG).show()
                    val enableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Mam uprawnienia", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(getApplicationContext(), "Nie mam uprawnien", Toast.LENGTH_LONG).show()
                        startActivity(enableIntent)
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "ble jest wyłączone", Toast.LENGTH_LONG).show()
                }

            }
        }

    }
}