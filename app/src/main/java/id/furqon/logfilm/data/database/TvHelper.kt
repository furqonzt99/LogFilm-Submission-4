package id.furqon.logfilm.data.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion.TABEL_TV
import id.furqon.logfilm.data.database.DatabaseContract.LogFilmColumns.Companion._ID
import java.sql.SQLException

class TvHelper(context: Context) {

    companion object {
        private const val TABEL_DATABASE = TABEL_TV
        private lateinit var dataBaseTvHelper: DatabaseLogFilmHelper
        private lateinit var database: SQLiteDatabase

        private var INSTANCE: TvHelper? = null

        fun getInstance(context: Context): TvHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            TvHelper(
                                context
                            )
                    }
                }
            }
            return INSTANCE as TvHelper
        }
    }

    init {
        dataBaseTvHelper =
            DatabaseLogFilmHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseTvHelper.writableDatabase
    }

    fun close() {
        dataBaseTvHelper.close()

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