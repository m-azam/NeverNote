package infrrd.ai.nevernote.objects

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

object VolleyConfig {

    lateinit var requestQueue: RequestQueue

    fun init(context: Context) {
        requestQueue = Volley.newRequestQueue(context)
    }

    fun <T> addToRequestQueue(request: Request<T>) {
        requestQueue.add(request)
    }

}

