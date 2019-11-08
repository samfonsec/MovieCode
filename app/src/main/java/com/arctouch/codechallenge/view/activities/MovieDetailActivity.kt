package com.arctouch.codechallenge.view.activities

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat.getFont
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.service.model.Movie
import com.arctouch.codechallenge.util.*
import com.arctouch.codechallenge.viewModel.MovieDetailViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.act_movie_detail.*


class MovieDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieDetailViewModel
    private val movieImageUrlBuilder = MovieImageUrlBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_movie_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = createViewModel(MovieDetailViewModel::class.java)

        setUpObservables()
        setupView()
        getMovieDetail()
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

    private fun setupView() {
        with(collapsingToolbar) {
            setCollapsedTitleTextColor(getColor(R.color.colorText))
            setExpandedTitleTextColor(ColorStateList.valueOf(Color.WHITE))
            setCollapsedTitleTypeface(getFont(this@MovieDetailActivity, R.font.open_sans))
            setExpandedTitleTypeface(getFont(this@MovieDetailActivity, R.font.open_sans))
        }
    }

    private fun setUpObservables() {
        viewModel.onDetailResult.observe(this, Observer { onMovieResult(it) })
        viewModel.onError.observe(this, Observer { onError() })
    }

    private fun onMovieResult(movie: Movie?) {
        hideProgress()
        movie?.let {
            collapsingToolbar.visibility = VISIBLE
            collapsingToolbar.title = it.title
            tvOverviewBody.text = it.overview
            tvReleaseDate.text = it.releaseDate
            tvGenresBody.text = it.genres?.joinToString(separator = ", ") { genre -> genre.name }
            setAppBarImage(it.backdropPath)
            val rating = it.voteAverage / 2
            ratingBar.rating = rating
            ratingTextView.text = String.format("%.1f", rating)
            tvGenresTitle.visibility = if (it.genres.isNullOrEmpty()) GONE else VISIBLE
            tvOverviewTitle.visibility = if (it.overview.isNullOrEmpty()) GONE else VISIBLE
        }
    }

    private fun setAppBarImage(imageUrl: String?) {
        if (imageUrl.isNullOrEmpty()) {
            appbarImage.setBackgroundColor(getColor(R.color.colorGray))
        } else {
            Glide.with(this)
                    .load(movieImageUrlBuilder.buildPosterUrl(imageUrl))
                    .into(appbarImage)
        }
    }

    private fun onError() {
        hideProgress()
        showErrorDialog(DialogInterface.OnClickListener { dialog, _ ->
            getMovieDetail()
            dialog.dismiss()
        })
    }

    private fun getMovieDetail() {
        showProgress()
        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, Int.MAX_VALUE)
        viewModel.getMovieDetail(movieId)
    }

    companion object {
        private const val EXTRA_MOVIE_ID = "extra_movie_id"

        fun newIntent(context: Context, movieId: Int?) =
                Intent(context, MovieDetailActivity::class.java).apply {
                    putExtra(EXTRA_MOVIE_ID, movieId)
                }
    }
}