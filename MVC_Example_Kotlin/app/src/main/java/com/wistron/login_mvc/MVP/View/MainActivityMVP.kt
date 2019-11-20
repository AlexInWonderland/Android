package com.wistron.login_mvc.MVP.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.wistron.login_mvc.MVP.Presenter.Presenter
import com.wistron.login_mvc.Model.PWDModel
import com.wistron.login_mvc.R

class MainActivityMVP : AppCompatActivity(), MyView {
    lateinit var presenter:Presenter
    lateinit var et_account:EditText
    lateinit var et_pwd:EditText
    lateinit var button: Button
    override fun clearEditText() {
        et_account.setText("")
        et_pwd.setText("")
    }

    override fun toastMsg(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    override fun setContentView() {
        setContentView(R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //etContentView(R.layout.activity_main_mvp)
        presenter = Presenter(this, PWDModel())
        presenter.onCreate()
        et_pwd = findViewById(R.id.et_pwd)
        et_account = findViewById(R.id.et_account)
        button = findViewById(R.id.btn_login)
        button.setOnClickListener { presenter.onloginClick(et_account.text.toString(), et_pwd.text.toString()) }
    }
}
