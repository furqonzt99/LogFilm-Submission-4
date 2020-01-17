package id.furqon.logfilm.ui.movielist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import id.furqon.logfilm.data.API_KEY
import id.furqon.logfilm.data.BASE_URL
import id.furqon.logfilm.data.moviemodel.MovieItems
import org.json.JSONObject

class MovieViewModel : ViewModel() {

    val listMovie = MutableLiveData<ArrayList<MovieItems>>()

    internal fun setMovie(languange: String) {
        val client = AsyncHttpClient()
        val listItem = ArrayList<MovieItems>()
        val url = BASE_URL + "discover/movie?api_key=$API_KEY&language=$languange&page=1"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")

                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        val movieItem = MovieItems()
                        movieItem.id = movie.getInt("id")
                        movieItem.title = movie.getString("title")
                        movieItem.vote_average = movie.getDouble("vote_average") / 2
                        movieItem.release_date = movie.getString("release_date")
                        movieItem.poster_path = movie.getString("poster_path")
                        listItem.add(movieItem)
                    }
                    listMovie.postValue(listItem)
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

    internal fun getMovies(): LiveData<ArrayList<MovieItems>> {
        return listMovie
    }
}