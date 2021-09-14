package com.gallapillo.newsbreak.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gallapillo.newsbreak.R
import com.gallapillo.newsbreak.databinding.FragmentArticleBinding
import com.gallapillo.newsbreak.view.NewsActivity
import com.gallapillo.newsbreak.viewmodel.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel
    private lateinit var mBinding: FragmentArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentArticleBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).mViewModel
    }

    companion object {
        @JvmStatic
        fun newInstance() = ArticleFragment()
    }
}