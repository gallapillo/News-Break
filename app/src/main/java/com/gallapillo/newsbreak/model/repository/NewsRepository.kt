package com.gallapillo.newsbreak.model.repository

import com.gallapillo.newsbreak.model.api.RetrofitInstance
import com.gallapillo.newsbreak.model.db.ArticleDatabase

/* Главный репозиторий Проекта*/
class NewsRepository(
    val db: ArticleDatabase
) {
    /* Получение новостей */
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)
}