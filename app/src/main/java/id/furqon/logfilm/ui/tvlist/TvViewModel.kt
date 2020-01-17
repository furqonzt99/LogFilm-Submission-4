package id.furqon.logfilm.ui.tvlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import id.furqon.logfilm.data.API_KEY
import id.furqon.logfilm.data.BASE_URL
import id.furqon.logfilm.data.tvmodel.TvItems
import org.json.JSONObject

class TvViewModel : ViewModel() {
    val listTv = MutableLiveData<ArrayList<TvItems>>()

    internal fun setTv(languange: String) {
        val client = AsyncHttpClient()
        val listItem = ArrayList<TvItems>()
        val url = BASE_URL + "discover/tv?api_key=$API_KEY&language=$languange&page=1"

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
                        val tv = list.getJSONObject(i)
                        val tvItem = TvItems()
                        tvItem.id = tv.getInt("id")
                        tvItem.name = tv.getString("name")
                        tvItem.vote_average = tv.getDouble("vote_average") / 2
                        tvItem.first_air_date = tv.getString("first_air_date")
                        tvItem.poster_path = tv.getString("poster_path")
                        listItem.add(tvItem)
                    }
                    listTv.postValue(listItem)
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

    internal fun getTv(): LiveData<ArrayList<TvItems>> {
        return listTv
    }
}