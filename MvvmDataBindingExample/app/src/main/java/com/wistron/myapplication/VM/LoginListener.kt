package com.wistron.myapplication.VM

interface LoginListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(msg:String)
}