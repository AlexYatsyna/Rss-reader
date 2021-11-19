package com.example.tests.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleViewModel(application: Application): AndroidViewModel(application) {

    lateinit var lastArticles :  LiveData<List<Article>>

    val repository: ArticleRepository


    init {
        val dao = ArticleRoomDatabase.getDatabase(application).getNotesDao()
        repository = ArticleRepository(dao)
        lastArticles = repository.lastArticles
    }


    fun deleteArticle(article: Article)= viewModelScope.launch (Dispatchers.IO)
    {
        repository.delete(article)
    }

    fun addArticle(article: Article)= viewModelScope.launch (Dispatchers.IO)
    {
        repository.insert(article)
    }


    fun updateArticle(article: Article)= viewModelScope.launch (Dispatchers.IO)
    {
        repository.update(article)
    }

}