package com.gallapillo.newsbreak.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gallapillo.newsbreak.R
import com.gallapillo.newsbreak.databinding.FragmentArticleBinding
import com.gallapillo.newsbreak.view.NewsActivity
import com.gallapillo.newsbreak.view.adapters.NewsAdapter
import com.gallapillo.newsbreak.viewmodel.ArticleViewModel
import com.gallapillo.newsbreak.viewmodel.NewsViewModel
import com.gallapillo.newsbreak.viewmodel.UrlViewModel
import com.gallapillo.newsbreak.z_utils.showToast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_saved_news.*

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var mViewModel: NewsViewModel
    private lateinit var mBinding: FragmentArticleBinding
    lateinit var mNewsAdapter: NewsAdapter
    private val mUrlViewModel: UrlViewModel by activityViewModels()
    private val mArticleViewModel: ArticleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentArticleBinding.inflate(layoutInflater)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = (activity as NewsActivity).mViewModel
        mUrlViewModel.url.observe(viewLifecycleOwner, {
            webView.apply {
                webViewClient = WebViewClient()
                loadUrl(it)
            }
        })

        mArticleViewModel.article.observe(viewLifecycleOwner, { article ->

            fab.setOnClickListener {
                mViewModel.saveArticle(article)
                Snackbar.make(view, "News saved successfully!", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView() {
        mNewsAdapter = NewsAdapter()
        rvSavedNews.apply {
            adapter = mNewsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ArticleFragment()
    }
}