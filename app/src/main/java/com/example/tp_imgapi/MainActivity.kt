package com.example.tp_imgapi
//api키 utVlSl2opjj9e4zRIfI0WQAiY74VUgTPcW6rvQJWSWmzELOUdIhnOZq7
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.tp_imgapi.model.PexelsResponse
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView: ImageView = findViewById(R.id.imageView)
        requestQueue = Volley.newRequestQueue(this)

        val url = "https://api.pexels.com/v1/curated"
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val gson = Gson()
                val pexelsResponseType = object : TypeToken<PexelsResponse>() {}.type
                val pexelsResponse: PexelsResponse = gson.fromJson(response.toString(), pexelsResponseType)
                val photo = pexelsResponse.photos[0]
                Glide.with(this)
                    .load(photo.src.large)
                    .into(imageView)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "utVlSl2opjj9e4zRIfI0WQAiY74VUgTPcW6rvQJWSWmzELOUdIhnOZq7" // Replace with your Pexels API key
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }

    override fun onStop() {
        super.onStop()
        requestQueue.cancelAll(this)
    }
}