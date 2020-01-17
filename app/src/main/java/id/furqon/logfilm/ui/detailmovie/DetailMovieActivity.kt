package id.furqon.logfilm.ui.detailmovie

import android.content.ContentValues
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import id.furqon.logfilm.R
import id.furqon.logfilm.data.BACKDROP_SIZE
import id.furqon.logfilm.data.BASE_IMAGE_URL
import id.furqon.logfilm.data.POSTER_SIZE
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.JUDUL
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.POSTER_PATH
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.RATING
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.TANGGAL_RILIS
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion._ID
import id.furqon.logfilm.data.database.MovieHelper
import id.furqon.logfilm.data.moviemodel.MovieDetails
import kotlinx.android.synthetic.main.movie_detail.*
import java.util.*

class DetailMovieActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailMovieViewModel
    private lateinit var languange: String

    private lateinit var movieHelper: MovieHelper

    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movieId: Int = intent.getIntExtra("id", 0)
        val moviePoster: String = intent.getStringExtra("poster")

        languange = Locale.getDefault().toLanguageTag().toString()

        shimmerFrameLayout = findViewById(R.id.shimmer_detail)

        movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.open()

        viewModel =
            ViewModelProviders.of(this, ViewModelProvider.NewInstanceFactory())
                .get(DetailMovieViewModel::class.java)

        viewModel.setMovieDetails(movieId, languange)

        showLoading(true)

        viewModel.getMovieDetails().observe(this, Observer {
            bindUI(it)
            showLoading(false)
        })

        //cek & set favorit
        val cursor = movieHelper.queryById(movieId.toString())
        val idMov = mutableListOf<Int>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                idMov.add(id)
            }
        }

        if (idMov.size > 0) {
            setTombolFavorit(true)
        } else {
            setTombolFavorit(false)
        }

        //aksi favorit
        btnSuka.setOnClickListener {
            if (detailJudul.text.toString().isNotEmpty() && detailRilis.text.toString().isNotEmpty() && detailRatingText.text.toString().isNotEmpty()) {
                setFavorit(movieId, moviePoster)
            } else {
                Toast.makeText(this, getString(R.string.waitData), Toast.LENGTH_SHORT).show()
            }
        }

        //aksi unfavorit
        btnTidakSuka.setOnClickListener {
            setUnFavorit(movieId)
        }
    }

    private fun setTombolFavorit(state: Boolean) {
        if (state) {
            btnTidakSuka.visibility = View.VISIBLE
            btnSuka.visibility = View.GONE
        } else {
            btnTidakSuka.visibility = View.GONE
            btnSuka.visibility = View.VISIBLE
        }
    }

    private fun setUnFavorit(movieId: Int) {
        val judul = detailJudul.text.toString().trim()
        val result = movieHelper.deleteById("$movieId").toLong()
        if (result > 0) {
            setTombolFavorit(false)
            Toast.makeText(
                this,
                getString(R.string.berhasil_mengapus) + " $judul " + getString(R.string.dari_favorit),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this,
                getString(R.string.gagal_menghapus) + " $judul " + getString(R.string.dari_favorit),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setFavorit(movieId: Int, moviePoster: String) {
        val judul = detailJudul.text.toString().trim()
        val rating = detailRatingText.text.toString().toDouble()
        val tanggalRilis = detailRilis.text.toString().trim()

        val values = ContentValues()
        values.put(_ID, movieId)
        values.put(POSTER_PATH, moviePoster)
        values.put(JUDUL, judul)
        values.put(RATING, rating)
        values.put(TANGGAL_RILIS, tanggalRilis)

        val result = movieHelper.insert(values)

        if (result > 0) {
            setTombolFavorit(true)
            Toast.makeText(
                this,
                "$judul " + getString(R.string.berhasil_ditambah),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(this, "$judul " + getString(R.string.gagal_ditambah), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            details_layout.visibility = View.GONE
            shimmerFrameLayout.startShimmerAnimation()
            shimmerFrameLayout.visibility = View.VISIBLE
        } else {
            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.visibility = View.GONE
            details_layout.visibility = View.VISIBLE
        }
    }

    private fun bindUI(it: MovieDetails) {
        detailJudul.text = it.title
        detailRilis.text = it.release_date
        detailRating.rating = it.vote_average!!.toFloat() / 2
        detailRatingText.text = it.vote_average.toString()
        detailDurasi.text = it.runtime.toString() + getString(R.string.minutes)

        if (it.tagline == "") {
            detailTagline.text = getString(R.string.ifNull)
        } else {
            detailTagline.text = it.tagline
        }
        detailStatus.text = it.status

        if (it.overview == "") {
            detailSinopsis.text = getString(R.string.ifNull)
        } else {
            detailSinopsis.text = it.overview
        }

        if (it.adult.toString() == "false") detailGolongan.text =
            getString(R.string.all_ages) else getString(
            R.string.adult
        )

        val moviePosterURL = BASE_IMAGE_URL + POSTER_SIZE + it.poster_path
        Glide.with(this)
            .load(moviePosterURL)
            .into(detailPoster)

        val movieBackdropURL = BASE_IMAGE_URL + BACKDROP_SIZE + it.backdrop_path
        Glide.with(this)
            .load(movieBackdropURL)
            .into(background_poster)
    }

    override fun onDestroy() {
        super.onDestroy()
        movieHelper.close()
    }
}
