package infrrd.ai.nevernote.services

import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import infrrd.ai.nevernote.objects.VolleyConfig

class ServiceCalls() {
    val url = "http://192.168.0.118:8080/getallnote"
    fun getAllNotes(callback: (String?) -> Unit) {
        var responseString: String? = null
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    callback(response)
                },
                Response.ErrorListener { error ->
                    Log.d("Call", error.toString())
                })
        VolleyConfig.addToRequestQueue(stringRequest)
    }
}