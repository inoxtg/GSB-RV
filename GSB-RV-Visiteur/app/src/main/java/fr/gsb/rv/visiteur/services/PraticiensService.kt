package fr.gsb.rv.visiteur.services

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import fr.gsb.rv.visiteur.BuildConfig
import fr.gsb.rv.visiteur.entites.Praticien

class PraticiensService {

    companion object {

        private val ip: String = BuildConfig.SERVER_URL
        private lateinit var requestQueue: RequestQueue

        fun getLesPraticiens(): MutableList<Praticien> {
            val url = "$ip/praticiens"
            val praticiens = mutableListOf<Praticien>()

            val request = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    var i = 0
                    while (i < response.length()){
                        val praticien = Praticien(
                            Integer.parseInt(response.getJSONObject(i).getString("pra_num")),
                            response.getJSONObject(i).getString("pra_nom"),
                            response.getJSONObject(i).getString("pra_prenom"),
                            response.getJSONObject(i).getString("pra_ville"),
                            response.getJSONObject(i).getString("pra_cp"))
                        praticiens.add(praticien)
                        i += 1
                    }
                },
                {
                    Log.i("Error Praticien : ", it.toString())
                })
            requestQueue.add(request)
            return praticiens
        }

    }
}