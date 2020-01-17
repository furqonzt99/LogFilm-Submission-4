package id.furqon.logfilm.data.helper

import android.database.Cursor
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.JUDUL
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.POSTER_PATH
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.RATING
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.TANGGAL_RILIS
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion._ID
import id.furqon.logfilm.data.moviemodel.MovieItems
import id.furqon.logfilm.data.tvmodel.TvItems

object MappingHelper {

    fun mapMovieCursorToArrayList(moviesCursor: Cursor): ArrayList<MovieItems> {
        val moviesList = ArrayList<MovieItems>()
        while (moviesCursor.moveToNext()) {
            val id =
                moviesCursor.getInt(moviesCursor.getColumnIndexOrThrow(_ID))
            val posterPath =
                moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(POSTER_PATH))
            val judul =
                moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(JUDUL))
            val voteAverage =
                moviesCursor.getDouble(moviesCursor.getColumnIndexOrThrow(RATING))
            val tanggalRilis =
                moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(TANGGAL_RILIS))
            moviesList.add(MovieItems(id, posterPath, tanggalRilis, judul, voteAverage))
        }
        return moviesList
    }

    fun mapTvCursorToArrayList(tvsCursor: Cursor): ArrayList<TvItems> {
        val tvsList = ArrayList<TvItems>()
        while (tvsCursor.moveToNext()) {
            val id =
                tvsCursor.getInt(tvsCursor.getColumnIndexOrThrow(_ID))
            val posterPath =
                tvsCursor.getString(tvsCursor.getColumnIndexOrThrow(POSTER_PATH))
            val judul =
                tvsCursor.getString(tvsCursor.getColumnIndexOrThrow(JUDUL))
            val voteAverage =
                tvsCursor.getDouble(tvsCursor.getColumnIndexOrThrow(RATING))
            val tanggalRilis =
                tvsCursor.getString(tvsCursor.getColumnIndexOrThrow(TANGGAL_RILIS))
            tvsList.add(TvItems(id, posterPath, tanggalRilis, judul, voteAverage))
        }
        return tvsList
    }

}