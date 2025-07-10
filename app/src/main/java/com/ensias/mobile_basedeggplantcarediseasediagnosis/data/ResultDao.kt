package com.ensias.mobile_basedeggplantcarediseasediagnosis.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageResultDao {
    @Insert
    suspend fun insert(imageResult: ImageResult)

    @Query("SELECT * FROM result")
    fun getAllImageResult(): Flow<List<ImageResult>>


    @Query("SELECT * FROM result WHERE id =:resultId LIMIT 1")
    fun getResultById(resultId:Int): Flow<ImageResult>

    @Delete
    suspend fun delete(imageResult: ImageResult)

}
