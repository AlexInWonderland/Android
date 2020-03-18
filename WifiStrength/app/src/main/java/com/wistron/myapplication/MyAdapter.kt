package com.wistron.myapplication

import android.net.wifi.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import java.util.*

class MyAdapter(arrayList: List<ScanResult>) : RecyclerView.Adapter<MyAdapter.ExampleViewHolder>(){
    private var mExampleList:MutableList<ScanResult> = arrayList.toMutableList()
    class ExampleViewHolder(
        itemView: View
    ) :
        ViewHolder(itemView) {
        var mBSSID: TextView
        var mSSID: TextView
        var mlevel: TextView

        init {
            mBSSID = itemView.findViewById(R.id.textView)
            mSSID = itemView.findViewById(R.id.textView1)
            mlevel = itemView.findViewById(R.id.textView2)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.example_item, parent, false)
        return ExampleViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mExampleList.size
    }

    fun addItem(scanResult:ScanResult){
        mExampleList.add(scanResult)
    }

    fun clearList(){
        mExampleList.clear()
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem: ScanResult = mExampleList[position]
        holder.mBSSID.setText(currentItem.BSSID)
        holder.mSSID.setText(currentItem.SSID)
        holder.mlevel.setText(currentItem.level.toString())
    }
}