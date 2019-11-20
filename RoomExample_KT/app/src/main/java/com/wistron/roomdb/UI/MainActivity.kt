package com.wistron.roomdb.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.wistron.roomdb.DB.Pokemon_Eitity
import com.wistron.roomdb.DB.poekmonDB
import com.wistron.roomdb.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var db = Room.databaseBuilder(applicationContext, poekmonDB::class.java, "poekDB").build()

        Thread{
            var pika = Pokemon_Eitity()
            pika.name = "richel"
            pika.id = 2
            pika.img="richel.jpg"
            db.pokeDAO().savePoke(pika)
            db.pokeDAO().getAllPoke().forEach{
                Log.i("@Alex", "id is: ${it.id}")
                Log.i("@Alex", "name is: ${it.name}")
                Log.i("@Alex", "img is: ${it.img}")
            }
        }.start()
    }
}
