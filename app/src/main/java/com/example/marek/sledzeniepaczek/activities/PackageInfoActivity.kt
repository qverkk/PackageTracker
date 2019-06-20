package com.example.marek.sledzeniepaczek.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.example.marek.sledzeniepaczek.R
import com.example.marek.sledzeniepaczek.customListView.HistoryCustomAdapter
import com.example.marek.sledzeniepaczek.data.Packages
import com.example.marek.sledzeniepaczek.data.ServerHelper
import com.example.marek.sledzeniepaczek.data.ServerResponse
import com.example.marek.sledzeniepaczek.helpers.DatabaseHelper
import kotlinx.android.synthetic.main.activity_package_info.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup


class PackageInfoActivity : AppCompatActivity() {

    lateinit var databaseHelper: DatabaseHelper

    private lateinit var adapter: ArrayAdapter<String>

    private var adapterList = arrayListOf("Loading...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.marek.sledzeniepaczek.R.layout.activity_package_info)
        databaseHelper = DatabaseHelper(this)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, adapterList)
        trackingHistoryList.adapter = adapter

        val packages: Packages = intent.getSerializableExtra("package") as Packages
        courierNameInfo.text = packages.courier.courierName
        packageNumberInfo.text = packages.packageNumber
        removeButton(packages)
        fillListThread(packages)
    }

    private fun initializeListeners() {
        openMapButton.setOnClickListener {
            val response: ServerResponse = trackingHistoryList.adapter.getItem(0) as ServerResponse
            println(response.location)
            if (response.location != "NAN" && !response.location.isNullOrEmpty()) {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("location", response.location)
                startActivity(intent)
            } else {
                Snackbar.make(
                    trackingHistoryList,
                    "Unable to find location",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("No action", null).show()
            }
        }
    }

    private fun removeButton(packages: Packages) {
        removePackageInfo.setOnClickListener {
            if (databaseHelper.deleteData(packages)) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Snackbar.make(it, "Couldn't delete data", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }

    private fun fillListThread(packages: Packages) {
        val dialog = ProgressDialog.show(
            this@PackageInfoActivity, "",
            "Loading. Please wait...", true
        )

        val cont = this

        GlobalScope.launch {
            val link = packages.courier.websiteUrl(packages.packageNumber).toString()

            val doc = Jsoup.connect(link)

            dialog.show()

            try {
                val response = doc.execute()
                if (response.statusCode() == 200) {
                    val responseBody = response.body()

                    runOnUiThread {
                        adapterList.clear()
                        val serverHelper = ServerHelper()
                        val serverResponseList = serverHelper.convertToResponses(responseBody)
                        if (packages.courier.courierName == "Poczta polska")
                            serverResponseList.reverse()
                        val adapter = HistoryCustomAdapter(serverResponseList, R.layout.row_history, cont)
                        trackingHistoryList.adapter = adapter
                        dialog.hide()
                        initializeListeners()
                    }
                } else {
                    runOnUiThread {
                        adapterList.clear()
                        adapterList.add("Please reload the page.")
                        adapter.notifyDataSetChanged()
                        dialog.hide()
                        openMapButton.isEnabled = false
                    }
                }
            } catch (exception: Exception) {
                runOnUiThread {
                    adapterList.clear()
                    adapterList.add("Error: SocketTimeoutException! Please reload the page.")
                    adapter.notifyDataSetChanged()
                    dialog.hide()
                    openMapButton.isEnabled = false
                }
            }
        }
    }
}