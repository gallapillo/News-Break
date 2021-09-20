package com.gallapillo.newsbreak.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gallapillo.newsbreak.view.adapters.NewsAdapter
import com.gallapillo.newsbreak.R
import com.gallapillo.newsbreak.databinding.FragmentBreakingNewsBinding
import com.gallapillo.newsbreak.view.NewsActivity
import com.gallapillo.newsbreak.viewmodel.ArticleViewModel
import com.gallapillo.newsbreak.viewmodel.NewsViewModel
import com.gallapillo.newsbreak.viewmodel.UrlViewModel
import com.gallapillo.newsbreak.z_utils.Resource
import com.gallapillo.newsbreak.z_utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    lateinit var mViewModel: NewsViewModel
    lateinit var mNewsAdapter: NewsAdapter
    private lateinit var mBinding: FragmentBreakingNewsBinding
    private var mUrlPage = ""
    private val mUrlViewModel: UrlViewModel by activityViewModels()
    private val mArticleViewModel: ArticleViewModel by activityViewModels()

    private val mTAG = "BreakingNewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentBreakingNewsBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = (activity as NewsActivity).mViewModel
        setupRecyclerView()

        mNewsAdapter.setOnItemClickListener { article ->
            mUrlPage = article.url
            //showToast(mUrlPage)
            mArticleViewModel.article.value = article
            mUrlViewModel.url.value = article.url
            //showToast(mUrlViewModel.url.value.toString())
            replaceFragment(ArticleFragment.newInstance())
        }

        mViewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        mNewsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(mTAG, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        mNewsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = mNewsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = BreakingNewsFragment()
    }
}