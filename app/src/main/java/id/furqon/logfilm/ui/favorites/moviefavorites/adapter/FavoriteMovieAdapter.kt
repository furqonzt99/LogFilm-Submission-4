package id.furqon.logfilm.ui.favorites.moviefavorites.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.furqon.logfilm.R
import id.furqon.logfilm.data.BASE_IMAGE_URL
import id.furqon.logfilm.data.POSTER_SIZE
import id.furqon.logfilm.data.moviemodel.MovieItems
import id.furqon.logfilm.ui.detailmovie.DetailMovieActivity
import kotlinx.android.synthetic.main.item_view.view.*

class FavoriteMovieAdapter(private val context: Context?) :
    RecyclerView.Adapter<FavoriteMovieAdapter.MovieViewHolder>() {

    var listMovies = ArrayList<MovieItems>()
        set(listMovies) {
            this.listMovies.clear()
            this.listMovies.addAll(listMovies)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = this.listMovies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(listMovies[position])
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movieItems: MovieItems) {
            with(itemView) {
                listJudul.text = movieItems.title
                listRatingBar.rating = movieItems.vote_average!!.toFloat() / 2
                listRealeaseDate.text = movieItems.release_date

                Glide.with(itemView)
                    .load(BASE_IMAGE_URL + POSTER_SIZE + movieItems.poster_path)
                    .into(listPoster)
            }

            itemView.setOnClickListener {
                val i = Intent(context, DetailMovieActivity::class.java)
                i.putExtra("id", movieItems.id)
                i.putExtra("poster", movieItems.poster_path)
                context?.startActivity(i)
            }
        }

    }
}