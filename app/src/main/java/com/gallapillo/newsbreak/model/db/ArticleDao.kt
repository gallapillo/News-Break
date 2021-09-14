package com.gallapillo.newsbreak.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gallapillo.newsbreak.model.Article

/* Интерфейс DAO для работы с БД вставка получение всех элементов
*  Живой даты и удаление новости и Вставка
* */
@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}