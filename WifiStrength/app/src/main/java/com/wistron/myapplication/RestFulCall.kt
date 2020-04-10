package com.wistron.myapplication

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class RestFulCall:Callback {
    private var onRequestCompleteListener : OnRequestCompleteListener? =null

    fun addListener(onRequestCompleteListener: OnRequestCompleteListener){
        this.onRequestCompleteListener = onRequestCompleteListener
    }
    override fun onFailure(call: Call, e: IOException) {
        e.printStackTrace()   //if no internet unknown host exception
        onRequestCompleteListener?.onError()
        println("error")
    }

    override fun onResponse(call: Call, response: Response) {
        var result:MutableList<ScanResultJson> = mutableListOf()
        if (response.isSuccessful) {
            val body = response.body?.string()
            val jsonObject = JSONObject(body)
            val rows = JSONArray(jsonObject.get("rows").toString())
            for(i in 0 until rows.length()) {
                val item = rows.getJSONObject(i)
                //PhoneMac.phoneMacCompared = item.get("phoneMac").toString()
                val wifiList = JSONArray(item.get("wifiList").toString())
                for(i in 0 until wifiList.length()){
                    val item = wifiList.getJSONObject(i)
                    println(item)
                    result.add(ScanResultJson(item.getString("macAddress"), item.getString("name"), item.getInt("signalStrength"), item.getInt("frequency")))
                }
            }
        }
        onRequestCompleteListener?.onSuccess(result.toList())
    }
}

interface OnRequestCompleteListener{
    fun onSuccess(scanResults:List<ScanResultJson>)
    fun onError()
}