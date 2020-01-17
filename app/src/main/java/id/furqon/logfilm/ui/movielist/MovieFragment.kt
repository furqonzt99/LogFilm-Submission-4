package id.furqon.logfilm.ui.movielist


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import id.furqon.logfilm.R
import kotlinx.android.synthetic.main.fragment_movie.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */

class MovieFragment : Fragment() {

    private lateinit var adapter: MovieAdapter
    private lateinit var viewModel: MovieViewModel
    private lateinit var root: View

    private lateinit var languange: String

    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_movie, container, false)

        shimmerFrameLayout = root.findViewById(R.id.shimmer_recyclerview)

        adapter = MovieAdapter()
        adapter.notifyDataSetChanged()

        root.rv_movies.layoutManager = LinearLayoutManager(activity)
        root.rv_movies.adapter = adapter

        viewModel =
            ViewModelProviders.of(this, ViewModelProvider.NewInstanceFactory())
                .get(MovieViewModel::class.java)

        languange = Locale.getDefault().toLanguageTag().toString()

        viewModel.setMovie(languange)

        showLoading(true)

        viewModel.getMovies().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.setData(it)
                showLoading(false)
            }
        })

        return root
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
}
