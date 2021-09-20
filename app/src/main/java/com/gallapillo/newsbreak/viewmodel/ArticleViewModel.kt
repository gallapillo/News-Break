package com.gallapillo.newsbreak.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gallapillo.newsbreak.model.Article

class ArticleViewModel : ViewModel() {
    val article: MutableLiveData<Article> by lazy {
        MutableLiveData<Article>()
    }

}