package com.gallapillo.newsbreak.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gallapillo.newsbreak.R
import com.gallapillo.newsbreak.databinding.FragmentSavedNewsBinding
import com.gallapillo.newsbreak.view.NewsActivity
import com.gallapillo.newsbreak.view.adapters.NewsAdapter
import com.gallapillo.newsbreak.viewmodel.ArticleViewModel
import com.gallapillo.newsbreak.viewmodel.NewsViewModel
import com.gallapillo.newsbreak.viewmodel.UrlViewModel
import com.gallapillo.newsbreak.z_utils.replaceFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_news.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    lateinit var mViewModel: NewsViewModel
    private lateinit var mBinding: FragmentSavedNewsBinding
    lateinit var mNewsAdapter: NewsAdapter
    private val mUrlViewModel: UrlViewModel by activityViewModels()
    private val mArticleViewModel: ArticleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSavedNewsBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        mViewModel = (activity as NewsActivity).mViewModel
        mNewsAdapter.setOnItemClickListener { article ->
            mArticleViewModel.article.value = article
            mUrlViewModel.url.value = article.url
            replaceFragment(ArticleFragment.newInstance())
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = mNewsAdapter.differ.currentList[position]
                mViewModel.deleteArticle(article)
                Snackbar.make(view, "Sucessfully delete news", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo") {
                        mViewModel.saveArticle(article)
                    }
                    show()
                }
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedNews)
        }

        mViewModel.getSavedNews().observe(viewLifecycleOwner, { articles ->
            mNewsAdapter.differ.submitList(articles)

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
        fun newInstance() = SavedNewsFragment()
    }
}