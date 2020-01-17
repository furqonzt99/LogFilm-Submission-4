package id.furqon.logfilm.ui.tvlist


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
import kotlinx.android.synthetic.main.fragment_tv_show.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class TvFragment : Fragment() {

    private lateinit var adapter: TvAdapter
    private lateinit var viewModel: TvViewModel
    private lateinit var root: View

    private lateinit var languange: String

    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_tv_show, container, false)

        shimmerFrameLayout = root.findViewById(R.id.shimmer_recyclerview)

        adapter = TvAdapter()
        adapter.notifyDataSetChanged()

        root.rv_tv_shows.layoutManager = LinearLayoutManager(activity)
        root.rv_tv_shows.adapter = adapter

        viewModel =
            ViewModelProviders.of(this, ViewModelProvider.NewInstanceFactory())
                .get(TvViewModel::class.java)

        languange = Locale.getDefault().toLanguageTag().toString()

        viewModel.setTv(languange)

        showLoading(true)

        viewModel.getTv().observe(viewLifecycleOwner, Observer {
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
