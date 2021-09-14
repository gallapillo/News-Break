package com.gallapillo.newsbreak.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.gallapillo.newsbreak.R
import com.gallapillo.newsbreak.databinding.ActivityNewsBinding
import com.gallapillo.newsbreak.model.db.ArticleDatabase
import com.gallapillo.newsbreak.model.repository.NewsRepository
import com.gallapillo.newsbreak.view.fragments.BreakingNewsFragment
import com.gallapillo.newsbreak.view.fragments.SavedNewsFragment
import com.gallapillo.newsbreak.view.fragments.SearchNewsFragment
import com.gallapillo.newsbreak.viewmodel.NewsViewModel
import com.gallapillo.newsbreak.viewmodel.NewsViewModelProviderFactory
import com.gallapillo.newsbreak.z_utils.APP_ACTIVITY
import com.gallapillo.newsbreak.z_utils.replaceFragment

class NewsActivity : AppCompatActivity() {

    lateinit var mViewModel: NewsViewModel
    private lateinit var mBinding: ActivityNewsBinding

    /* Главная активити */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        APP_ACTIVITY = this
        mBinding = ActivityNewsBinding.inflate(layoutInflater)

        replaceFragment(BreakingNewsFragment.newInstance())

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        mViewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
    }

    fun onBreakNewsButtonDown(item: MenuItem) {
        replaceFragment(BreakingNewsFragment.newInstance())
        item.isChecked = true
    }

    fun onSavedNewsButtonDown(item: MenuItem) {
        replaceFragment(SavedNewsFragment.newInstance())
        item.isChecked = true
    }

    fun onSearchNewsButtonDown(item: MenuItem) {
        replaceFragment(SearchNewsFragment.newInstance())
        item.isChecked = true
    }
}
