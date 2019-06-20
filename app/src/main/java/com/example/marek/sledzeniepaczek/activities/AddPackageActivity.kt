package com.example.marek.sledzeniepaczek.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.marek.sledzeniepaczek.R
import com.example.marek.sledzeniepaczek.helpers.DatabaseHelper
import kotlinx.android.synthetic.main.activity_add_package.*

class AddPackageActivity : AppCompatActivity() {

    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_package)

        databaseHelper = DatabaseHelper(this)

        initializeListeners()
    }

    private fun initializeListeners() {
        cancelPackageBtn.setOnClickListener {
            launchMainActivity()
        }

        addPackageBtn.setOnClickListener {
            val number = packageNumber.text.toString()
            if (number.isNotEmpty()) {
                if (databaseHelper.addData(courierList.selectedItem.toString(), number)) {
                    makeToast("Added new data")
                    launchMainActivity()
                } else {
                    makeToast("Failed to add new data")
                }
            } else {
                Snackbar.make(it, "Packages number is empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}