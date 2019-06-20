package com.example.marek.sledzeniepaczek.customListView

import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.marek.sledzeniepaczek.R
import com.example.marek.sledzeniepaczek.activities.PackageInfoActivity
import com.example.marek.sledzeniepaczek.data.Packages

class CustomAdapter(val data: ArrayList<Packages>, val resource: Int, val mcontext: Context) :
    ArrayAdapter<Packages>(mcontext, resource, data), View.OnClickListener {


    private class ViewHolder {
        lateinit var listPackageNumber: TextView
        lateinit var courierName: TextView
        lateinit var info: ImageView
    }

    override fun onClick(v: View?) {
        val position = v?.tag as Int
        val obj = getItem(position)
        val data = obj as Packages

        when (v.id) {
            R.id.itemInfo -> {
                Snackbar.make(
                    v,
                    "Package number: ${data.packageNumber}, courier: ${data.courier}",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("No action", null).show()
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val data = getItem(position) as Packages

        val holder: ViewHolder
        val ratView: View

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            ratView = inflater.inflate(resource, parent, false)
            holder = ViewHolder()

            holder.courierName = ratView.findViewById(R.id.courierName) as TextView
            holder.info = ratView.findViewById(R.id.itemInfo) as ImageView
            holder.listPackageNumber = ratView.findViewById(R.id.listPackageNumber) as TextView

            ratView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            ratView = convertView
        }

        holder.listPackageNumber.text = data.packageNumber
        holder.courierName.text = data.courier.courierName
        holder.info.setOnClickListener {
            Snackbar.make(
                ratView,
                "Package number: ${data.packageNumber}, courier: ${data.courier}",
                Snackbar.LENGTH_LONG
            ).setAction("No action", null).show()
        }

        ratView.setOnClickListener {
            val intent = Intent(this.context, PackageInfoActivity::class.java)
            intent.putExtra("package", data)
            context.startActivity(intent)
        }

        return ratView
    }
}