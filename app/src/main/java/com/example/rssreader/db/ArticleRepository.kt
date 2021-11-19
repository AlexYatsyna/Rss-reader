package com.example.tests.db


import androidx.lifecycle.LiveData



class ArticleRepository(private  val articlesDao: ArticlesDao) {

    var lastArticles :  LiveData<List<Article>> = articlesDao.getLastNotes()


    fun select() :  LiveData<List<Article>>
    {
        return articlesDao.getLastNotes()
    }

    suspend fun insert(article: Article)
    {
        articlesDao.insert(article)
    }


    suspend fun delete(article: Article)
    {
        articlesDao.delete(article)
    }

    suspend fun update(article: Article)
    {
        articlesDao.update(article)
    }

}