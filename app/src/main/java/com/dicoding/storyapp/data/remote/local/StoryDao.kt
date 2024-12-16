package com.dicoding.storyapp.data.remote.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(story: List<StoryEntity>)

    @Query("SELECT * FROM StoryEntity")
    fun getStory(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM StoryEntity")
    suspend fun deleteAll()
}