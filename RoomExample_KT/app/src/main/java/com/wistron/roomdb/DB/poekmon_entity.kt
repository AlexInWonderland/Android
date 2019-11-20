package com.wistron.roomdb.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_table")
class Pokemon_Eitity
{
    @PrimaryKey
    var id: Int = 0

    @ColumnInfo(name = "P_NAME")
    var name:String = ""
    @ColumnInfo(name = "IMG")
    var img:String = ""

}