package com.wistron.myapplication

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.wistron.myapplication.VM.LoginListener

class LoginViewModel : ViewModel() {
    var account:String ?= null
    var password:String ?= null
    var loginListener: LoginListener?= null

    fun onLoginClick(view: View){
        loginListener?.onStarted()

        if(account.isNullOrBlank() || password.isNullOrBlank())
        {
             loginListener?.onFailure("invalid user")
            return
        }
        loginListener?.onSuccess()
        Toast.makeText(view.context, "Onclick called" + account + password,Toast.LENGTH_SHORT).show()
    }
}