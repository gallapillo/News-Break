package com.gallapillo.newsbreak.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UrlViewModel : ViewModel() {
    val url: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

}