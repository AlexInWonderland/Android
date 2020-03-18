package com.wistron.myapplication

import com.google.gson.annotations.SerializedName

class Group{
    @SerializedName("id")
    var id:Int = 0

    @SerializedName("members")
    var _members:List<String> ?= null

}