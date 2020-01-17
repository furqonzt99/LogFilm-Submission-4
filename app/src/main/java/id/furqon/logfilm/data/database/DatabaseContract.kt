package id.furqon.logfilm.data.database

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class LogFilmColumns : BaseColumns {
        companion object {
            const val TABEL_MOVIE = "movie"
            const val TABEL_TV = "tv"
            const val _ID = "_id"
            const val POSTER_PATH = "poster_path"
            const val JUDUL = "judul"
            const val RATING = "rating"
            const val TANGGAL_RILIS = "tanggal_rilis"
        }
    }
}