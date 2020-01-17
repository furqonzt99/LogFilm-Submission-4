package id.furqon.logfilm.data.tvmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvItems(
    var id: Int = 0,
    var poster_path: String? = null,
    var first_air_date: String? = null,
    var name: String? = null,
    var vote_average: Double? = null
) : Parcelable