package id.furqon.logfilm.ui.favorites.moviefavorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import id.furqon.logfilm.R
import id.furqon.logfilm.data.database.MovieHelper
import id.furqon.logfilm.data.helper.MappingHelper
import id.furqon.logfilm.data.moviemodel.MovieItems
import id.furqon.logfilm.ui.favorites.moviefavorites.adapter.FavoriteMovieAdapter
import kotlinx.android.synthetic.main.fragment_movie.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoritesMovieFragment : Fragment() {

    private lateinit var adapter: FavoriteMovieAdapter
    private lateinit var root: View

    private lateinit var movieHelper: MovieHelper

    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_movie, container, false)

        shimmerFrameLayout = root.findViewById(R.id.shimmer_recyclerview)

        adapter = FavoriteMovieAdapter(context)
        adapter.notifyDataSetChanged()

        root.rv_movies.layoutManager = LinearLayoutManager(activity)
        root.rv_movies.adapter = adapter

        movieHelper = MovieHelper.getInstance(activity!!.applicationContext)
        movieHelper.open()

        if (savedInstanceState == null) {
            //ambil data
            loadMoviesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<MovieItems>(EXTRA_STATE)
            if (list != null) {
                adapter.listMovies = list
            }
        }
        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listMovies)
    }

    private fun loadMoviesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val defferedMovies = async(Dispatchers.IO) {
                val cursor = movieHelper.queryAll()
                MappingHelper.mapMovieCursorToArrayList(cursor)
            }
            showLoading(false)
            val movies = defferedMovies.await()

            if (movies.size > 0) {
                adapter.listMovies = movies
            } else {
                adapter.listMovies = movies
                Toast.makeText(activity, getString(R.string.nullMoviesFav), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            shimmerFrameLayout.startShimmerAnimation()
            shimmerFrameLayout.visibility = View.VISIBLE
        } else {
            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        movieHelper.open()
        loadMoviesAsync()
    }

    override fun onPause() {
        super.onPause()
        movieHelper.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        movieHelper.close()
    }
}