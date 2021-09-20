package com.gallapillo.newsbreak.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gallapillo.newsbreak.view.adapters.NewsAdapter
import com.gallapillo.newsbreak.R
import com.gallapillo.newsbreak.databinding.FragmentBreakingNewsBinding
import com.gallapillo.newsbreak.view.NewsActivity
import com.gallapillo.newsbreak.viewmodel.ArticleViewModel
import com.gallapillo.newsbreak.viewmodel.NewsViewModel
import com.gallapillo.newsbreak.viewmodel.UrlViewModel
import com.gallapillo.newsbreak.z_utils.Constants.Companion.QUERY_PAGE_SIZE
import com.gallapillo.newsbreak.z_utils.Resource
import com.gallapillo.newsbreak.z_utils.replaceFragment
import com.gallapillo.newsbreak.z_utils.showToast
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
                        mNewsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = mViewModel.breakingNewsPage == totalPages
                        if (isLastPage) {
                            rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(mTAG, "An error occurred: $message")
                        showToast("An error occurred: $message")
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
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.itemCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                mViewModel.getBreakingNews("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        mNewsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = mNewsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = BreakingNewsFragment()
    }
}