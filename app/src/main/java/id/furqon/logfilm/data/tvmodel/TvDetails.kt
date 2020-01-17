package id.furqon.logfilm.data.tvmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvDetails(
    var number_of_episodes: Int? = 0,
    var backdrop_path: String? = null,
    var id: Int? = 0,
    var overview: String? = null,
    var poster_path: String? = null,
    var first_air_date: String? = null,
    var popularity: String? = null,
    var status: String? = null,
    var number_of_seasons: Int? = 0,
    var name: String? = null,
    var vote_average: Double? = null
) : Parcelable
