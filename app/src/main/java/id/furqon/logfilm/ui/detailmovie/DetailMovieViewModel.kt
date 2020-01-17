package id.furqon.logfilm.ui.detailmovie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import id.furqon.logfilm.data.API_KEY
import id.furqon.logfilm.data.BASE_URL
import id.furqon.logfilm.data.moviemodel.MovieDetails
import org.json.JSONObject

class DetailMovieViewModel : ViewModel() {

    val movieDetails = MutableLiveData<MovieDetails>()

    internal fun setMovieDetails(movieId: Int, language: String) {
        val client = AsyncHttpClient()
        val url = BASE_URL + "movie/$movieId?api_key=$API_KEY&language=$language"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val details = JSONObject(result)

                    val movies = MovieDetails()

                    movies.id = details.getInt("id")
                    movies.backdrop_path = details.getString("backdrop_path")
                    movies.poster_path = details.getString("poster_path")
                    movies.title = details.getString("title")
                    movies.release_date = details.getString("release_date")
                    movies.runtime = details.getInt("runtime")
                    movies.tagline = details.getString("tagline")
                    movies.status = details.getString("status")
                    movies.adult = details.getBoolean("adult")
                    movies.vote_average = details.getDouble("vote_average")
                    movies.overview = details.getString("overview")

                    movieDetails.postValue(movies)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
            }

        })
    }

    internal fun getMovieDetails(): LiveData<MovieDetails> {
        return movieDetails
    }

}