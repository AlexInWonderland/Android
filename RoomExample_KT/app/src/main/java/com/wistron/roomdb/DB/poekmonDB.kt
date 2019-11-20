package com.wistron.roomdb.DB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(Pokemon_Eitity::class)], version = 1)
abstract class poekmonDB : RoomDatabase(){
    abstract fun pokeDAO() : pokemonDAO
}