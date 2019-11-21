package com.wistron.login_mvc.mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.wistron.login_mvc.R
import com.wistron.login_mvc.databinding.ActivityMainMvvmBinding

//Controller
class MainActivityMVVM : AppCompatActivity() {
    //private lateinit var button:Button
    //private lateinit var et_pwd:EditText
    //private lateinit var et_account:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
         val binding:ActivityMainMvvmBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_mvvm)
         val viewModel: LoginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
         binding.alexviewmodel = viewModel
    }
}
