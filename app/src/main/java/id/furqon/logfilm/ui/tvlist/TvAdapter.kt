package id.furqon.logfilm.ui.tvlist

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.furqon.logfilm.R
import id.furqon.logfilm.data.BASE_IMAGE_URL
import id.furqon.logfilm.data.POSTER_SIZE
import id.furqon.logfilm.data.tvmodel.TvItems
import id.furqon.logfilm.ui.detailtv.DetailTvActivity
import kotlinx.android.synthetic.main.item_view.view.*

class TvAdapter : RecyclerView.Adapter<TvAdapter.TvViewHolder>() {
    private val mData = ArrayList<TvItems>()

    fun setData(items: ArrayList<TvItems>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TvViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return TvViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: TvViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class TvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvItems: TvItems) {
            with(itemView) {
                listJudul.text = tvItems.name
                listRatingBar.rating = tvItems.vote_average?.toFloat()!!
                listRealeaseDate.text = tvItems.first_air_date

                Glide.with(itemView)
                    .load(BASE_IMAGE_URL + POSTER_SIZE + tvItems.poster_path)
                    .into(listPoster)

                itemView.setOnClickListener {
                    val i = Intent(context, DetailTvActivity::class.java)
                    i.putExtra("id", tvItems.id)
                    i.putExtra("poster", tvItems.poster_path)
                    context.startActivity(i)
                }
            }
        }
    }
}