package com.arctouch.codechallenge.view.activities

import android.arch.lifecycle.Observer
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.service.model.Movie
import com.arctouch.codechallenge.util.*
import com.arctouch.codechallenge.view.adapters.HomeAdapter
import com.arctouch.codechallenge.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.act_home.*


class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: HomeViewModel
    private var currentPage: Int = FIRST_PAGE
    private lateinit var homeAdapter: HomeAdapter
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_home)
        setSupportActionBar(toolbar)

        viewModel = createViewModel(HomeViewModel::class.java)

        setUpObservers()
        setUpRecyclerView()
        getGenres()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setUpObservers() {
        viewModel.onMovieUpcomingResult.observe(this, Observer { onMovieUpcomingResult(it) })
        viewModel.onError.observe(this, Observer { onError() })
    }

    private fun setUpRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                getMoviesUpcoming(++currentPage)
            }
        })
    }

    private fun onMovieUpcomingResult(response: ArrayList<Movie>?) {
        hideProgress()
        isLoading = false
        response?.let { movies ->
            isLastPage = movies.isEmpty()
            if (currentPage == FIRST_PAGE) {
                homeAdapter = HomeAdapter(movies)
                homeAdapter.onItemClicked.observe(this, Observer {
                    startActivity(MovieDetailActivity.newIntent(this, it))
                })
                recyclerView.adapter = homeAdapter
            } else {
                homeAdapter.addMovies(movies)
            }
        }
    }

    private fun onError() {
        hideProgress()
        showErrorDialog(DialogInterface.OnClickListener { dialog, _ ->
            getMoviesUpcoming(currentPage)
            dialog.dismiss()
        })
    }

    private fun getGenres() {
        showProgress()
        viewModel.getGenres(currentPage)
    }

    private fun getMoviesUpcoming(page: Int) {
        showProgress()
        viewModel.getMoviesUpcoming(page)
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}
