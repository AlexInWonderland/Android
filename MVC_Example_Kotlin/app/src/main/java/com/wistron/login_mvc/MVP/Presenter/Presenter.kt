package com.wistron.login_mvc.MVP.Presenter

import android.util.Log
import com.wistron.login_mvc.Model.PWDModel
import com.wistron.login_mvc.MVP.View.MyView

public class Presenter constructor(myView: MyView, pwdModel: PWDModel) {
    private lateinit var myView: MyView
    private lateinit var pwdModel: PWDModel

    init{
        this.myView = myView
        this.pwdModel = pwdModel
    }

    fun onCreate(){
        Log.d("Presenter", "OnCreate")
        myView.setContentView()
    }

    fun onloginClick(account:String, pwd:String){
        Log.d("Presenter", "onloginclick")
        pwdModel.checkaccount(account, pwd)
        myView.clearEditText()
        myView.toastMsg(pwdModel.checksuccessmsg())
    }

}