package com.wistron.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.os.Looper
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow


class WifiScanReceiver() : BroadcastReceiver(), OnRequestCompleteListener{
    var timer = 0
    var prev_wifiList: List<ScanResult> = emptyList()
    var prev_wifi_mutableMap: MutableMap<String, ScanResultChannelFreq> = mutableMapOf()
    var applicationContext:Context ?= null
    var mAdapter:MyAdapter ?= null
    var restFulCall:RestFulCall ?= null
    var currentList:List<ScanResult> = emptyList()
    var thirtySec_wifi_mutableMap: MutableMap<String, ScanResultCount> = mutableMapOf()
    constructor(applicationContext: Context, mAdapter: MyAdapter) : this() {
        this.applicationContext = applicationContext
        this.mAdapter = mAdapter
        this.restFulCall = RestFulCall()
        this.restFulCall!!.addListener(this)
        PhoneMac.mac = PhoneMac.getMacAddress()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(c: Context, intent: Intent) {
        if (intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)) { //New scan results are available. Arrange a callback to the activity here.
            println("On receive is called!")

            val wifiManager = c.getSystemService(Context.WIFI_SERVICE) as WifiManager
            var wifiList: List<ScanResult> = wifiManager.getScanResults()

            wifiList = wifiList.sortedWith(compareByDescending(ScanResult::level))
            val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
            val currentDateandTime: String = sdf.format(Date(System.currentTimeMillis() - 8 * 3600 * 1000))

            //var s = "$currentDateandTime,"
            //s += "${acc[0]}, ${acc[1]}, ${acc[2]}, ${gyro[0]}, ${gyro[1]}, ${gyro[2]}, "    //adding gyro data and linear acc data

            if(timer == 0){
                synchronized (this){
                    thirtySec_wifi_mutableMap.clear()
                    prev_wifi_mutableMap.clear()
                    prev_wifiList = emptyList()
                    prev_wifiList = wifiList
                    //GlobalVariables.output_prev_list = wifiList
                    //prev_wifiList = wifiList.sortedWith(compareByDescending(ScanResult::level))
                    val jsonArray : MutableList<ScanResultJson> = mutableListOf()
                    for (i in wifiList){
                        val scanResultJson = ScanResultJson(i.BSSID, i.SSID, i.level, i.frequency)
                        jsonArray.add(scanResultJson)
                    }
                    if(GlobalVariables.UPLOAD){
                        postData(PhoneMac.mac, jsonArray)
                    }
                }
            }
            timer++

            if(timer > 30) {
                //val sortedWifiList = wifiList
                val startDateandTime: String = sdf.format(Date(System.currentTimeMillis() - 8 * 3600 * 1000 - 1800*1000))
                currentList = wifiList
                //GlobalVariables.output_current_list = wifiList
                restFulGet(startDateandTime, currentDateandTime)
                timer = 0
            }
            mAdapter?.clearList()
            for (scanResult in wifiList) {
                mAdapter?.addItem(scanResult)
                if(thirtySec_wifi_mutableMap.contains(scanResult.BSSID)){
                    var scanResultCount: ScanResultCount? = thirtySec_wifi_mutableMap.get(scanResult.BSSID)
                    scanResultCount!!.signalStrength += scanResult.level
                    scanResultCount!!.signalStrength /= (2)
                    scanResultCount!!.appearCount += 1
                    thirtySec_wifi_mutableMap.put(scanResult.BSSID, scanResultCount!!)
                }
                else{
                    var scanResultTest = ScanResultCount(scanResult.level, 1)
                    thirtySec_wifi_mutableMap.put(scanResult.BSSID, scanResultTest)
                }
                //val level: Int = WifiManager.calculateSignalLevel(scanResult.level, 5)
                //val scanResultTimestampInMillisSinceEpoch = System.currentTimeMillis() - SystemClock.elapsedRealtime() + (scanResult.timestamp / 1000)
                //val s = "BSSID ${scanResult.BSSID}, SSID ${scanResult.SSID}, ${scanResult.level}, Level is $level out of 5, $currentDateandTime"
                //val ten = 10.0
                //val exp = (27.55 - 20 * Math.log10((scanResult.frequency.toDouble())) + Math.abs(scanResult.level))/20.0
                //val d = ten.pow(exp)
                //val df = DecimalFormat("#.##")
                //val d_two_digit = df.format(d)

                //s += "\"BSSID\":\"${scanResult.BSSID}\", \"level\": \"${scanResult.level}\", \"frequency\""
                //s += "${scanResult.BSSID}, ${scanResult.SSID}, ${scanResult.level}, ${scanResult.frequency}, $d_two_digit,"
                //println("BSSID ${scanResult.BSSID}, SSID ${scanResult.SSID}, ${scanResult.level}, Level is $level out of 5, frequency ${scanResult.frequency}, $currentDateandTime, distance $d_two_digit")
            }

            //s = s.substring(0, s.length - 1)
           //s +="\n"
            mAdapter?.notifyDataSetChanged()
            //writeToFile(s)
        }
    }

    fun postData(macAddress:String,jsonArray: MutableList<ScanResultJson>){
        val gson = Gson()
        val myJson:MyJsonData = MyJsonData(macAddress, jsonArray)
        val json = gson.toJson(myJson)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val request = json.toRequestBody(mediaType)
        restFulPost(request)
    }

    fun restFulGet(startDate:String, endDate:String){
        val client = OkHttpClient()
        val request: Request
        try{
            val url: HttpUrl = HttpUrl.Builder()
                //omit
                .addQueryParameter("PhoneMac", PhoneMac.phoneMacCompared)
                .build();
            request = Request.Builder()
                .addHeader("Content-Type", "applicantion/json")
                .url(url)
                .get()
                .build()
            val response = client.newCall(request)
            restFulCall?.let { response.enqueue(it) }
        }catch(e:Exception){
            e.printStackTrace()
        }
    }

    fun restFulPost(requestbody: RequestBody){   //1 post
        val client = OkHttpClient()
        val request: Request
        try{
           //omit
            request = Request.Builder()
                .addHeader("Content-Type", "applicantion/json")
                .url(apiURL)
                .post(requestbody)
                .build()

            val response = client.newCall(request)
            response.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    val responseStr = response.body
                }
            })
        }catch(e:Exception){
            e.printStackTrace()
        }
    }
    /*
     * Weighted cosine similarity
     *
     * */
    fun Cosine_Similarity_different_phone(sortedWifiList:List<ScanResult>, prev_wifiList:List<ScanResultJson>):Double{

        for(scanResult in prev_wifiList){
            var scanResultChannelFreq  = ScanResultChannelFreq(scanResult.signalStrength, scanResult.frequency)
            prev_wifi_mutableMap.put(scanResult.macAddress, scanResultChannelFreq)
        }
        val toast = Toast.makeText(applicationContext, "prev_wifiList " + prev_wifiList[0].name, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
        val toast1 = Toast.makeText(applicationContext, "sortedList " + sortedWifiList[0], Toast.LENGTH_LONG)
        toast1.setGravity(Gravity.LEFT, 0, 200)
        toast1.show()
        var dot_product:Float = 0f
        var A_square:Float = 0f
        var B_square:Float = 0f
        val weight_map = calculate_weights(sortedWifiList, thirtySec_wifi_mutableMap)

        var count:Int = 0
        for(i in sortedWifiList){
            if(prev_wifi_mutableMap.containsKey(i.BSSID)){
                count++
                var scanResultChannelFreq = prev_wifi_mutableMap.get(i.BSSID)
                if(i.frequency == scanResultChannelFreq!!.frequency){
                    if(weight_map.containsKey(i.BSSID)){
                        dot_product += weight_map.get(i.BSSID)!!.toFloat() * (i.level + 65) * (scanResultChannelFreq.signalStrength + 65)
                        A_square += weight_map.get(i.BSSID)!!.toFloat() * (i.level + 65).toFloat() .pow(2)
                        B_square += weight_map.get(i.BSSID)!!.toFloat() * (scanResultChannelFreq.signalStrength + 65)?.toFloat()!!.pow(2)
                    }
                    else{

                    }
                }
            }
           /* else{
                dot_product += (-1)*softmax_list[pick_count].toFloat() * (i.level + 65)
                A_square += softmax_list[pick_count].toFloat() * ((i.level + 65).toFloat()).pow(2)
            }*/
        }

        val w_cosine = dot_product/(Math.sqrt(A_square.toDouble()) * Math.sqrt(B_square.toDouble()))
        writeToFile("$w_cosine\n"+"AAAA")
        return w_cosine
        //return dot_product/(Math.sqrt(A_square.toDouble()) * Math.sqrt(B_square.toDouble()))
    }

    /*
     * use to calculate the weights for cosine
     *
     * */
    fun calculate_weights(x:List<ScanResult>, map:MutableMap<String, ScanResultCount>):MutableMap<String, Double>{
        var w_map:MutableMap<String, Double> = mutableMapOf()
        var sum = 0
        for(i in x){
            if(map.containsKey(i.BSSID)){
                sum += map.get(i.BSSID)!!.appearCount
                w_map.put(i.BSSID, map.get(i.BSSID)!!.appearCount.toDouble())
            }
        }

        for((k, v) in w_map){
            w_map[k] = v/sum
        }

        return w_map
    }

    /*
     * softmax for cosine similarity
     *
     * */
    fun softmax(x:List<ScanResult>):List<Double>{
        var exp_list:MutableList<Double> = mutableListOf()
        var exp_sum:Double = 0.0
        for(i in x){
            exp_sum += Math.exp(WifiManager.calculateSignalLevel(i.level, 5).toDouble())
        }
        //println("exp sum $exp_sum")
        for(i in x){
            val exp_i = Math.exp(WifiManager.calculateSignalLevel(i.level, 5).toDouble())
            exp_list.add(exp_i/exp_sum)
        }
        return exp_list.toList()
    }

    fun writeToFile(s:String){
        var sdCardPath = Environment.getExternalStorageDirectory().absolutePath
        sdCardPath += "/wistron_wifi"
        val file = File(sdCardPath, "wifi_strength.txt")
        try{
            if(!file.parentFile.exists()){
                file.parentFile.mkdir()
            }
            if(!file.exists()){
                file.createNewFile()
            }
            val fileWriter= FileWriter(file, true)
            val buffWriter = BufferedWriter(fileWriter)
            buffWriter.write(s)
            buffWriter.close()
        }
        catch(e: Exception){
            e.printStackTrace()
        }
    }

    override fun onSuccess(scanResults: List<ScanResultJson>) {
        Looper.prepare()
        var coss = 0.0
        if(scanResults.size != 0)
            coss = Cosine_Similarity_different_phone(currentList, scanResults)
             //java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()

        Toast.makeText(applicationContext, "Cosine Similarity: " + coss, Toast.LENGTH_SHORT).show()
        Looper.loop()
    }

    override fun onError() {
        println("Something is wrong with WifiReceiver.onError()")
    }
}