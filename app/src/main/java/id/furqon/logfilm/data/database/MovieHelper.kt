package id.furqon.logfilm.data.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.TABEL_MOVIE
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion._ID
import java.sql.SQLException

class MovieHelper(context: Context) {

    companion object {
        private const val TABEL_DATABASE = TABEL_MOVIE
        private lateinit var dataBaseLogFilmHelper: DatabaseLogFilmHelper
        private lateinit var database: SQLiteDatabase

        private var INSTANCE: MovieHelper? = null

        fun getInstance(context: Context): MovieHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            MovieHelper(
                                context
                            )
                    }
                }
            }
            return INSTANCE as MovieHelper
        }
    }

    init {
        dataBaseLogFilmHelper =
            DatabaseLogFilmHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseLogFilmHelper.writableDatabase
    }

    fun close() {
        dataBaseLogFilmHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            TABEL_DATABASE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            TABEL_DATABASE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(
            TABEL_DATABASE, null, values
        )
    }

    fun deleteById(id: String): Int {
        return database.delete(
            TABEL_DATABASE, "$_ID = '$id'", null
        )
    }
}