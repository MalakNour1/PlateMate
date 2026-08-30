package com.example.platemate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.platemate.data.local.entity.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_list")
    fun observeItems(): Flow<List<ShoppingListEntity>>

    // IGNORE, not REPLACE: if this ingredient is already on the list (added from
    // a different recipe earlier), keep its existing isChecked state instead of
    // resetting it back to unchecked.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<ShoppingListEntity>)

    @Query("UPDATE shopping_list SET isChecked = :isChecked WHERE id = :id")
    suspend fun setChecked(id: String, isChecked: Boolean)
}