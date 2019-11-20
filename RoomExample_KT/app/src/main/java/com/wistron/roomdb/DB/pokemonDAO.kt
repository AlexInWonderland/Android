package com.wistron.roomdb.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface pokemonDAO {

    @Insert
    fun savePoke(pokemonEitity: Pokemon_Eitity)

    @Query ("Select * from POKEMON_TABLE")
    fun getAllPoke():List<Pokemon_Eitity>


}