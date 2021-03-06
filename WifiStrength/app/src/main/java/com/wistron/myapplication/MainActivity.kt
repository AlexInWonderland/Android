package com.wistron.myapplication

//import jdk.nashorn.internal.objects.NativeDate.getTime

import android.content.Context
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(), SensorEventListener{
    lateinit var wifiManager: WifiManager
    lateinit var sensorManager: SensorManager
    private lateinit var wifiReceiver : WifiScanReceiver
    var acc = FloatArray(3)
    var gyro = FloatArray(3)
    lateinit var mLayouotManager:RecyclerView.LayoutManager
    lateinit var mAdapter:MyAdapter
    lateinit var mRecyclerView: RecyclerView
    var results:List<ScanResult> = emptyList()
    private var restFulCall:RestFulCall ?= null
    var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        registerListener()
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        mLayouotManager = LinearLayoutManager(this)
        mRecyclerView = findViewById(R.id.recyclerView)
        mRecyclerView.layoutManager = mLayouotManager
        mAdapter = MyAdapter(results)
        mRecyclerView.adapter = mAdapter

        wifiReceiver = WifiScanReceiver(applicationContext, mAdapter)
        registerReceiver(wifiReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        val handler = Handler()
        val locationUpdate: Unit = object : Runnable {
            override fun run() {
                getLocation()
                //This line will continuously call this Runnable with 1000 milliseconds gap
                handler.postDelayed(this, 1000)
            }
        }.run()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_test, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_switch ->
                if(counter % 2 ==0){
                    PhoneMac.phoneMacCompared = "AC:37:43:A1:60:C6"
                    Toast.makeText(applicationContext, "switched to compare AC:37:43:A1:60:C6", Toast.LENGTH_SHORT).show()
                }
                else{
                    PhoneMac.phoneMacCompared = "98:E7:9A:5B:EE:92"
                    Toast.makeText(applicationContext, "switched to compare 98:E7:9A:5B:EE:92", Toast.LENGTH_SHORT).show()
                }
            R.id.item_upload ->
                {
                    GlobalVariables.UPLOAD = !GlobalVariables.UPLOAD //toggle the upload function
                    if(GlobalVariables.UPLOAD)
                        Toast.makeText(applicationContext, "Start upload!", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(applicationContext, "Upload stopped!", Toast.LENGTH_SHORT).show()
                }
        }
        counter++
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(wifiReceiver)
        ungisterListener()
    }

    override fun onPause() {
        super.onPause()
        ungisterListener()
    }

    fun ungisterListener(){
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION))
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE))
    }

    override fun onResume() {
        super.onResume()
        registerListener()
    }

    fun registerListener(){
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun getLocation() {
        wifiManager.startScan();
        Log.d("START SCAN CALLED", "");
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event != null){
            when(event.sensor.type){
                Sensor.TYPE_LINEAR_ACCELERATION ->{
                    val xValue = Math.abs(event.values[0])
                    val yValue = Math.abs(event.values[1])
                    val zValue = Math.abs(event.values[2])
                    acc[0] = Math.abs(event.values[0])
                    acc[1] = Math.abs(event.values[1])
                    acc[2] = Math.abs(event.values[2])
                    //print("Alex acc x $xValue, y $yValue, z $zValue\n")
                }
                Sensor.TYPE_GYROSCOPE ->{
                    //val dT:Float = (event.timestamp - timestamp) * NS2S
                    //print("Alex gyro x angle ${event.values[0]}, y angle ${event.values[1]}, z ${event.values[2]}\n")
                    gyro[0] = Math.abs(event.values[0])
                    gyro[1] = Math.abs(event.values[1])
                    gyro[2] = Math.abs(event.values[2])
                }
            }
        }
    }


/*
    inner class WifiScanReceiver : BroadcastReceiver() {
        var timer = 0
        var prev_wifiList: List<ScanResult> = emptyList()
        var prev_wifi_mutableMap: MutableMap<String, Int> = mutableMapOf()

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceive(c: Context, intent: Intent) {
            if (intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)) { //New scan results are available. Arrange a callback to the activity here.
                println("On receive is called!")

                val wifiManager = c.getSystemService(Context.WIFI_SERVICE) as WifiManager
                var wifiList: List<ScanResult> = wifiManager.getScanResults()
                wifiList = wifiList.sortedWith(compareByDescending(ScanResult::level))
                val sdf = SimpleDateFormat("yyyy:MM:dd:HH:mm:ss", Locale.getDefault())
                val currentDateandTime: String = sdf.format(Date())
                var s:String = "$currentDateandTime,"
                //s += "${acc[0]}, ${acc[1]}, ${acc[2]}, ${gyro[0]}, ${gyro[1]}, ${gyro[2]}, "    //adding gyro data and linear acc data

                if(timer == 0){
                    prev_wifi_mutableMap.clear()
                    prev_wifiList = emptyList()
                    prev_wifiList = wifiList
                    //prev_wifiList = wifiList.sortedWith(compareByDescending(ScanResult::level))
                    val jsonArray : MutableList<ScanResultJson> = mutableListOf()
                    for (i in wifiList){
                        val scanResultJson = ScanResultJson(i.BSSID, i.SSID, i.level)
                        jsonArray.add(scanResultJson)
                    }
                    //postData(jsonArray)
                    restFulGet("2020/03/19 01:00:00", "2020/03/19 03:00:00")
                }
                timer++

                if(timer > 30) {
                    //val sortedWifiList = wifiList.sortedWith(compareByDescending(ScanResult::level))
                    val sortedWifiList = wifiList
                    var coss = Cosine_Similarity(sortedWifiList)

                    Toast.makeText(c, "Cosine Similarity: " + coss, Toast.LENGTH_SHORT).show()
                    timer = 0
                }
                mAdapter.clearList()
                for (scanResult in wifiList) {
                    mAdapter.addItem(scanResult)
                    val level: Int = WifiManager.calculateSignalLevel(scanResult.level, 5)
                    //val scanResultTimestampInMillisSinceEpoch = System.currentTimeMillis() - SystemClock.elapsedRealtime() + (scanResult.timestamp / 1000)
                    //val s = "BSSID ${scanResult.BSSID}, SSID ${scanResult.SSID}, ${scanResult.level}, Level is $level out of 5, $currentDateandTime"
                    val ten = 10.0
                    val exp = (27.55 - 20 * log10((scanResult.frequency.toDouble())) + abs(scanResult.level))/20.0
                    val d = ten.pow(exp)
                    val df = DecimalFormat("#.##")
                    val d_two_digit = df.format(d)

                    //s += "\"BSSID\":\"${scanResult.BSSID}\", \"level\": \"${scanResult.level}\", \"frequency\""
                    s += "${scanResult.BSSID}, ${scanResult.SSID}, ${scanResult.level}, ${scanResult.frequency}, $d_two_digit,"
                    //println("BSSID ${scanResult.BSSID}, SSID ${scanResult.SSID}, ${scanResult.level}, Level is $level out of 5, frequency ${scanResult.frequency}, $currentDateandTime, distance $d_two_digit")
                }
                s = s.substring(0, s.length - 1)
                s +="\n"
                mAdapter.notifyDataSetChanged()
                //writeToFile(s)
            }
        }

        fun postData(jsonArray: MutableList<ScanResultJson>){
            val gson = Gson()
            val json = gson.toJson(jsonArray)
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val request = json.toRequestBody(mediaType)
            restFulPost(request)
        }

        fun restFulGet(startDate:String, endDate:String){
            val client = OkHttpClient()
            val request: Request
            var result:MutableList<ScanResultJson> = mutableListOf()
            try{
                val url:HttpUrl = HttpUrl.Builder()
                    .scheme("https")
                    .host("healthinfotest.azurewebsites.net")
                    .addPathSegments("/api/Wifi/Info")
                    .addQueryParameter("StartDate", startDate)
                    .addQueryParameter("EndDate", endDate)
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
                val apiURL = "https://healthinfotest.azurewebsites.net/api/Wifi/Info"
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


        fun Cosine_Similarity(sortedWifiList:List<ScanResult>):Double{

            for(scanResult in prev_wifiList){
                prev_wifi_mutableMap.put(scanResult.BSSID, scanResult.level)
            }
            val toast = Toast.makeText(applicationContext, "prev_wifiList " + prev_wifiList[0], Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()
            val toast1 = Toast.makeText(applicationContext, "sortedList " + sortedWifiList[0], Toast.LENGTH_LONG)
            toast1.setGravity(Gravity.LEFT, 0, 200)
            toast1.show()
            var dot_product:Float = 0f
            var A_square:Float = 0f
            var B_square:Float = 0f
            val softmax_list = softmax(sortedWifiList)
            println(softmax_list)
            //if(sortedWifiList.size >= 4) {                 //pick the top 4
            //for (i in 0..3) {
            var pick_count = 0
                for(i in sortedWifiList ){
                    if(pick_count >= 10)
                        break;
                    if(prev_wifi_mutableMap.containsKey(i.BSSID)){
                        dot_product += softmax_list[pick_count].toFloat() * (i.level + 100) * (prev_wifi_mutableMap.get(i.BSSID)?.plus(
                            100
                        ))!!
                        A_square += softmax_list[pick_count].toFloat() * (i.level + 100).toFloat() .pow(2)
                        B_square += softmax_list[pick_count].toFloat() *(prev_wifi_mutableMap.get(i.BSSID)!! + 100)?.toFloat()!!.pow(2)
                    }
                    else{
                        dot_product += (-1)*softmax_list[pick_count].toFloat() * (i.level + 100)
                        A_square += softmax_list[pick_count].toFloat() * ((i.level + 100).toFloat()).pow(2)
                    }
                    pick_count++
                }
            //}
            var s:String = ""
            for(i in prev_wifiList)
                s += i.BSSID + " : ${i.level}" + ", "
            s+="\n"
            for(i in sortedWifiList)
                s += i.BSSID + " : ${i.level}" + ", "
            //writeToFile(s)
            return dot_product/(sqrt(A_square.toDouble()) * sqrt(B_square.toDouble()))
        }


        fun softmax(x:List<ScanResult>):List<Double>{
            var exp_list:MutableList<Double> = mutableListOf()
            var exp_sum:Double = 0.0
            for(i in x){
                exp_sum += exp(WifiManager.calculateSignalLevel(i.level, 5).toDouble())
            }
            //println("exp sum $exp_sum")
            for(i in x){
                val exp_i = exp(WifiManager.calculateSignalLevel(i.level, 5).toDouble())
                exp_list.add(exp_i/exp_sum)
            }
            return exp_list.toList()
        }

        fun writeToFile(s:String){
            var sdCardPath = getExternalStorageDirectory().absolutePath
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

    }
*/

}
