package com.example.calculatorapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val buttonSimple = findViewById<Button>(R.id.buttonSimple)
        buttonSimple.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val buttonAdvanced = findViewById<Button>(R.id.buttonAdvanced)
        buttonAdvanced.setOnClickListener {
            val intent = Intent(this, AdvancedActivity::class.java)
            startActivity(intent)
        }

        val buttonAbout = findViewById<Button>(R.id.buttonAbout)
        buttonAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
        val buttonExitApp = findViewById<Button>(R.id.buttonExitApp)
        buttonExitApp.setOnClickListener {
            finish()
            exitProcess(1)
        }
    }
}