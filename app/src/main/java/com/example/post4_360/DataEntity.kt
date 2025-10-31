package com.example.post4_360

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "citizen_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val nik: String,
    val kabupaten: String,
    val kecamatan: String,
    val desa: String,
    val rt: String,
    val rw: String,
    val gender: String,
    val status: String
)
