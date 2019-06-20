package com.example.marek.sledzeniepaczek.activities

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.marek.sledzeniepaczek.R
import com.example.marek.sledzeniepaczek.couriers.Couriers
import com.example.marek.sledzeniepaczek.customListView.CustomAdapter
import com.example.marek.sledzeniepaczek.data.Packages
import com.example.marek.sledzeniepaczek.helpers.DatabaseHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setTheme(R.style.FeedActivityThemeDark)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        databaseHelper = DatabaseHelper(this)

        populateListView()

        fab.setOnClickListener {
            val intent = Intent(this, AddPackageActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun populateListView() {
        val data = databaseHelper.getData()
        val listData = ArrayList<Packages>()

        while (data.moveToNext()) {
            val courier = Couriers.findCourier(data.getString(1))
            val packageNumber = data.getString(2)
            listData.add(Packages(courier, packageNumber))
        }

        val adapter = CustomAdapter(listData, R.layout.row_package_item, this)
        packageList.adapter = adapter
    }
}
