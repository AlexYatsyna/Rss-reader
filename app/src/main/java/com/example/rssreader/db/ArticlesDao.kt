package com.example.tests.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(article: Article)

    @Update
    suspend fun update(article: Article)

    @Delete
    suspend fun delete(article: Article)

    @Query("Select * from articles_table ORDER BY id DESC LIMIT 10")
    fun getLastNotes():  LiveData<List<Article>>




}