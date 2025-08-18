package com.berkang.cookbook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data  class Recipe(

    @ColumnInfo(name="name")
    var name : String,
    @ColumnInfo(name="content")
    var material: String,
    @ColumnInfo(name="image")
    var pimage:ByteArray

) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}