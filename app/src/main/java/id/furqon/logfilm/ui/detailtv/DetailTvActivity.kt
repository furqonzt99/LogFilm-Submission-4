@file:Suppress("DEPRECATION")

package id.furqon.logfilm.ui.detailtv

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
import id.furqon.logfilm.data.database.TvHelper
import id.furqon.logfilm.data.tvmodel.TvDetails
import kotlinx.android.synthetic.main.tv_detail.*
import java.util.*

class DetailTvActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailTvViewModel
    private lateinit var languange: String

    private lateinit var tvHelper: TvHelper

    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tv_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tvId: Int = intent.getIntExtra("id", 1)
        val tvPoster: String = intent.getStringExtra("poster")

        languange = Locale.getDefault().toLanguageTag().toString()

        shimmerFrameLayout = findViewById(R.id.shimmer_detail)

        tvHelper = TvHelper.getInstance(applicationContext)
        tvHelper.open()

        viewModel =
            ViewModelProviders.of(this, ViewModelProvider.NewInstanceFactory())
                .get(DetailTvViewModel::class.java)

        viewModel.setTvDetails(tvId, languange)

        showLoading(true)

        viewModel.getTvDetails().observe(this, Observer {
            bindUI(it)
            showLoading(false)
        })

        //cek & set favorit
        val cursor = tvHelper.queryById(tvId.toString())
        val idTv = mutableListOf<Int>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                idTv.add(id)
            }
        }

        if (idTv.size > 0) {
            setTombolFavorit(true)
        } else {
            setTombolFavorit(false)
        }

        //aksi favorit
        btnSuka.setOnClickListener {
            if (detailJudul.text.toString().isNotEmpty() && detailRilis.text.toString().isNotEmpty() && detailRatingText.text.toString().isNotEmpty()) {
                setFavorit(tvId, tvPoster)
            } else {
                Toast.makeText(this, getString(R.string.waitData), Toast.LENGTH_SHORT).show()
            }
        }

        //aksi unfavorit
        btnTidakSuka.setOnClickListener {
            setUnFavorit(tvId)
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

    private fun setUnFavorit(tvId: Int) {
        val judul = detailJudul.text.toString().trim()
        val result = tvHelper.deleteById("$tvId").toLong()
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

    private fun setFavorit(tvId: Int, tvPoster: String) {
        val judul = detailJudul.text.toString().trim()
        val rating = detailRatingText.text.toString().toDouble()
        val tanggalRilis = detailRilis.text.toString().trim()

        val values = ContentValues()
        values.put(_ID, tvId)
        values.put(POSTER_PATH, tvPoster)
        values.put(JUDUL, judul)
        values.put(RATING, rating)
        values.put(TANGGAL_RILIS, tanggalRilis)

        val result = tvHelper.insert(values)

        if (result > 0) {
            setTombolFavorit(true)
            Toast.makeText(
                this,
                "$judul " + getString(R.string.berhasil_ditambah),
                Toast.LENGTH_SHORT
            )
                .show()
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

    private fun bindUI(it: TvDetails) {
        detailJudul.text = it.name
        detailRilis.text = it.first_air_date
        detailRating.rating = it.vote_average!!.toFloat() / 2
        detailRatingText.text = it.vote_average.toString()
        detailDurasi.text = it.number_of_episodes.toString()
        detailTagline.text = it.number_of_seasons.toString()
        detailStatus.text = it.status

        if (it.overview == "") {
            detailSinopsis.text = getString(R.string.ifNull)
        } else {
            detailSinopsis.text = it.overview
        }

        detailGolongan.text = it.popularity

        val tvPosterURL = BASE_IMAGE_URL + POSTER_SIZE + it.poster_path
        Glide.with(this)
            .load(tvPosterURL)
            .into(detailPoster)

        val tvBackdropURL = BASE_IMAGE_URL + BACKDROP_SIZE + it.backdrop_path
        Glide.with(this)
            .load(tvBackdropURL)
            .into(background_poster)
    }
}
