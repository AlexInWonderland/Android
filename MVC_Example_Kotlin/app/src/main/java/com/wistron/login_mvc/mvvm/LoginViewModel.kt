package com.wistron.login_mvc.mvvm

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.wistron.login_mvc.Model.PWDModel

class LoginViewModel: ViewModel() {
      var account :String ?= null
      var pwd:String ?= null
      var pwdModel:PWDModel = PWDModel()
      fun onLoginClick(view: View){
          if(account.isNullOrBlank() || pwd.isNullOrBlank()){
                Toast.makeText(view.context, "null account or password", Toast.LENGTH_SHORT).show()
                return
          }
          else{
                 pwdModel.checkaccount(account!!, pwd!!)
          }
          Toast.makeText(view.context, "mvvm "+pwdModel.checksuccessmsg(), Toast.LENGTH_SHORT).show()
      }
}