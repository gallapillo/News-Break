package com.gallapillo.newsbreak.model

/* Модель запроса новостей */
data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)