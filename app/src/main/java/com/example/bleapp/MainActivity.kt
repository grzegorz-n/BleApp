package com.example.bleapp

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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

                    //tworzę tablicę uprwanień
                    val myPermissions: Array<String> = arrayOf(android.Manifest.permission.BLUETOOTH,
                        android.Manifest.permission.BLUETOOTH_ADMIN,
                        android.Manifest.permission.BLUETOOTH_CONNECT,
                        android.Manifest.permission.BLUETOOTH_SCAN,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)

                    //tworzę zmienną która informuje czy mam uprawnienia
                    var doPremissions = myPermissions.all {
                        (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED)
                    }


                    if (doPremissions) {
                        //tu mam uprawnienia
                        Toast.makeText(getApplicationContext(), "Mam uprawnienia", Toast.LENGTH_LONG).show()
                    } else {
                        // a tu się pytam o uprawnienia
                        requestPermissions(myPermissions, 1)
                    }
                    scanFunction(ble)
                    ble?.startDiscovery()

                } else {
                    // tutaj ble jest w ogóle wyłączone
                    Toast.makeText(getApplicationContext(), "ble jest wyłączone", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun scanFunction(ble: BluetoothAdapter): Unit {
        val deviceList: MutableList<BluetoothDevice> = mutableListOf<BluetoothDevice>()
        val receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when(intent?.action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        device?.let {
                            deviceList.add(it)
                        }
                    }
                }

            }
        }
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)




    }
}