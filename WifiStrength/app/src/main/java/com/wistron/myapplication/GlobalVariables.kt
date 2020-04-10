package com.wistron.myapplication

import android.net.wifi.ScanResult

object GlobalVariables {
    var UPLOAD:Boolean = false
    var output_prev_list:List<ScanResult> = emptyList()
    var output_current_list:List<ScanResult> = emptyList()
}