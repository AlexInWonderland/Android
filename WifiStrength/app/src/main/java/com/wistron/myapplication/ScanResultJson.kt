package com.wistron.myapplication

class ScanResultJson(bssid:String, ssid:String, strength:Int, freq:Int) {
    var macAddress:String = bssid
    var name:String = ssid
    var signalStrength:Int = strength
    var frequency:Int = freq     //only two 2000 5000 in Taiwan
}