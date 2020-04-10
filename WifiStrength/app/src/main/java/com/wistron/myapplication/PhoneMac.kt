package com.wistron.myapplication

import java.net.NetworkInterface
import java.util.*

object PhoneMac {
    var mac:String = "02:00:00:00:00:00"
    var phoneMacCompared = "98:E7:9A:5B:EE:92"
    fun getMacAddress(): String {
        try{
            val all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for(net_interface in all){
                if(!net_interface.name.equals("wlan0", true))
                    continue
                val macBytes = net_interface.hardwareAddress
                if(macBytes== null)
                    return ""
                val res1 = StringBuilder();
                for (b in macBytes) {
                    res1.append(String.format("%02X:",b));
                }
                if (res1.length > 0) {
                    res1.deleteCharAt(res1.length - 1);
                }
                return res1.toString();
            }

        }catch(e:Exception) {
            e.printStackTrace()
        }
        return "02:00:00:00:00:00";
    }
}