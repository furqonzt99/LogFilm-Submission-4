package id.furqon.logfilm.ui.detailtv

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import id.furqon.logfilm.data.API_KEY
import id.furqon.logfilm.data.BASE_URL
import id.furqon.logfilm.data.tvmodel.TvDetails
import org.json.JSONObject

class DetailTvViewModel : ViewModel() {

    val tvDetails = MutableLiveData<TvDetails>()

    internal fun setTvDetails(tvId: Int, language: String) {
        val client = AsyncHttpClient()
        val url = BASE_URL + "tv/$tvId?api_key=$API_KEY&language=$language"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val details = JSONObject(result)

                    val tv = TvDetails()

                    tv.id = details.getInt("id")
                    tv.backdrop_path = details.getString("backdrop_path")
                    tv.poster_path = details.getString("poster_path")
                    tv.name = details.getString("name")
                    tv.first_air_date = details.getString("first_air_date")
                    tv.number_of_episodes = details.getInt("number_of_episodes")
                    tv.number_of_seasons = details.getInt("number_of_seasons")
                    tv.status = details.getString("status")
                    tv.popularity = details.getString("popularity")
                    tv.vote_average = details.getDouble("vote_average")
                    tv.overview = details.getString("overview")

                    tvDetails.postValue(tv)

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

    internal fun getTvDetails(): LiveData<TvDetails> {
        return tvDetails
    }
}