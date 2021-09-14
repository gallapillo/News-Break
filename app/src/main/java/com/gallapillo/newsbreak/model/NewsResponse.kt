package com.gallapillo.newsbreak.model

/* Модель запроса новостей */
data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)