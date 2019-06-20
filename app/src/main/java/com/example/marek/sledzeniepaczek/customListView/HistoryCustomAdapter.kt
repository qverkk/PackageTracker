package com.example.marek.sledzeniepaczek.customListView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.marek.sledzeniepaczek.R
import com.example.marek.sledzeniepaczek.data.ServerResponse

class HistoryCustomAdapter(val data: ArrayList<ServerResponse>, val resource: Int, val mcontext: Context) :
    ArrayAdapter<ServerResponse>(mcontext, resource, data) {

    private class ViewHolder {
        lateinit var historyLocation: TextView
        lateinit var historyStatus: TextView
        lateinit var historyDate: TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val data = getItem(position) as ServerResponse

        val holder: ViewHolder
        val ratView: View

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            ratView = inflater.inflate(resource, parent, false)
            holder = ViewHolder()

            holder.historyLocation = ratView.findViewById(R.id.historyLocation) as TextView
            holder.historyStatus = ratView.findViewById(R.id.historyStatus) as TextView
            holder.historyDate = ratView.findViewById(R.id.historyDate) as TextView

            ratView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            ratView = convertView
        }

        holder.historyLocation.text = data.location
        holder.historyStatus.text = data.status
        holder.historyDate.text = data.time

        return ratView
    }


}