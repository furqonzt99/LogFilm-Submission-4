package id.furqon.logfilm.data.moviemodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetails(
    var adult: Boolean? = false,
    var backdrop_path: String? = null,
    var id: Int? = 0,
    var overview: String? = null,
    var poster_path: String? = null,
    var release_date: String? = null,
    var runtime: Int? = 0,
    var status: String? = null,
    var tagline: String? = null,
    var title: String? = null,
    var vote_average: Double? = null
) : Parcelable