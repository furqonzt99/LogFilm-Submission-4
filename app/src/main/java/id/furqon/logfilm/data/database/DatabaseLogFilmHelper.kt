package id.furqon.logfilm.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.JUDUL
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.POSTER_PATH
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.RATING
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.TABEL_MOVIE
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.TABEL_TV
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.TANGGAL_RILIS
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion._ID

class DatabaseLogFilmHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        NAMA_DATABASE, null,
        VERSI_DATABASE
    ) {
    companion object {
        private const val NAMA_DATABASE = "dblogfilm"
        private const val VERSI_DATABASE = 1
        private val SQL_CREATE_TABLE_MOVIE = "CREATE TABLE $TABEL_MOVIE" +
                " (${_ID} INTEGER PRIMARY KEY," +
                " ${POSTER_PATH} TEXT NOT NULL," +
                " ${JUDUL} TEXT NOT NULL," +
                " ${RATING} DOUBLE  NOT NULL," +
                " ${TANGGAL_RILIS} TEXT NOT NULL)"

        private val SQL_CREATE_TABLE_TV = "CREATE TABLE $TABEL_TV" +
                " (${_ID} INTEGER PRIMARY KEY," +
                " ${POSTER_PATH} TEXT NOT NULL," +
                " ${JUDUL} TEXT NOT NULL," +
                " ${RATING} DOUBLE  NOT NULL," +
                " ${TANGGAL_RILIS} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE)
        db.execSQL(SQL_CREATE_TABLE_TV)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABEL_MOVIE")
        db.execSQL("DROP TABLE IF EXISTS $TABEL_TV")
        onCreate(db)
    }
}