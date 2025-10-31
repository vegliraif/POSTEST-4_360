package com.example.post4_360

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataDao {
    @Insert
    suspend fun insert(data: DataEntity)

    @Query("SELECT * FROM citizen_table ORDER BY id DESC")
    suspend fun getAll(): List<DataEntity>

    @Query("DELETE FROM citizen_table")
    suspend fun deleteAll()
}
