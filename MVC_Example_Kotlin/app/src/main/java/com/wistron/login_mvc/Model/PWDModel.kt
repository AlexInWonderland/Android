package com.wistron.login_mvc.Model

import android.util.Log

class PWDModel{
    lateinit var pwd:String
    lateinit var account:String
    lateinit var msg:String
    init {
        pwd = "5566"
        account = "Alex"
    }

    fun checkaccount(account:String, pwd:String){
        Log.d("Model", "checkaccount")
        if(account.equals(this.account) && pwd.equals(this.pwd))
            msg = "login successfully"
        else
            msg = "login failed"
    }

    fun checksuccessmsg():String{
        return msg
    }
}