package com.wistron.login_mvc.MVC.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.wistron.login_mvc.Model.PWDModel
import com.wistron.login_mvc.R

//Controller
class MainActivityMVC : AppCompatActivity() {
    private lateinit var button:Button
    private lateinit var et_pwd:EditText
    private lateinit var et_account:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pwdModel: PWDModel = PWDModel()
        button = findViewById(R.id.btn_login)
        et_pwd = findViewById(R.id.et_pwd)
        et_account = findViewById(R.id.et_account)
        button.setOnClickListener {
            var account: String = et_account.text.toString()
            var pwd: String = et_pwd.text.toString()
            pwdModel.checkaccount(account, pwd)
            Toast.makeText(this, pwdModel.checksuccessmsg(), Toast.LENGTH_SHORT).show()
        }
    }
}
