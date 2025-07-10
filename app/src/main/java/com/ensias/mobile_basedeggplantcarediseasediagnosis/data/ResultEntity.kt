package com.ensias.mobile_basedeggplantcarediseasediagnosis.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "result",
)
data class ImageResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val  name: String,
    val image: ByteArray,
    val description: String
)
