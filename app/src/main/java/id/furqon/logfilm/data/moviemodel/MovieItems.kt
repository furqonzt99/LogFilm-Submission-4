package id.furqon.logfilm.data.moviemodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieItems(
    var id: Int = 0,
    var poster_path: String? = null,
    var release_date: String? = null,
    var title: String? = null,
    var vote_average: Double? = null
) : Parcelable