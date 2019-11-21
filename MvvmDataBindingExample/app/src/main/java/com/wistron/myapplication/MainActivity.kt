package com.wistron.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.wistron.myapplication.VM.LoginListener
import com.wistron.myapplication.databinding.ActivityMainBinding
import com.wistron.myapplication.utils.toast

class MainActivity : AppCompatActivity(), LoginListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val viewModel: LoginViewModel = ViewModelProviders.of(this).get(
            LoginViewModel::class.java)

        binding.viewmodel = viewModel

        viewModel.loginListener = this
    }

    override fun onStarted() {
        toast("stated")
    }

    override fun onSuccess() {
       toast("success")
    }

    override fun onFailure(msg: String) {
        toast(msg)
    }
}
