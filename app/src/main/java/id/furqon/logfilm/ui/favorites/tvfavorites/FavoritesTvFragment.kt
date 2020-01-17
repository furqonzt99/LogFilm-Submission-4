package id.furqon.logfilm.ui.favorites.tvfavorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import id.furqon.logfilm.R
import id.furqon.logfilm.data.database.TvHelper
import id.furqon.logfilm.data.helper.MappingHelper
import id.furqon.logfilm.data.tvmodel.TvItems
import id.furqon.logfilm.ui.favorites.tvfavorites.adapter.FavoriteTvAdapter
import kotlinx.android.synthetic.main.fragment_tv_show.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoritesTvFragment : Fragment() {


    private lateinit var adapter: FavoriteTvAdapter
    private lateinit var root: View

    private lateinit var tvHelper: TvHelper

    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_tv_show, container, false)

        shimmerFrameLayout = root.findViewById(R.id.shimmer_recyclerview)

        adapter = FavoriteTvAdapter(context)
        adapter.notifyDataSetChanged()

        root.rv_tv_shows.layoutManager = LinearLayoutManager(activity)
        root.rv_tv_shows.adapter = adapter

        tvHelper = TvHelper.getInstance(activity!!.applicationContext)
        tvHelper.open()

        if (savedInstanceState == null) {
            //ambil data
            loadMoviesAsync()
        } else {
            val list =
                savedInstanceState.getParcelableArrayList<TvItems>(EXTRA_STATE)
            if (list != null) {
                adapter.listTvs = list
            }
        }
        return root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listTvs)
    }

    private fun loadMoviesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val defferedMovies = async(Dispatchers.IO) {
                val cursor = tvHelper.queryAll()
                MappingHelper.mapTvCursorToArrayList(cursor)
            }
            showLoading(false)
            val tvs = defferedMovies.await()

            if (tvs.size > 0) {
                adapter.listTvs = tvs
            } else {
                adapter.listTvs = tvs
                Toast.makeText(activity, getString(R.string.nullTvsFav), Toast.LENGTH_SHORT).show()
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
        tvHelper.open()
        loadMoviesAsync()
    }

    override fun onPause() {
        super.onPause()
        tvHelper.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        tvHelper.close()
    }
}