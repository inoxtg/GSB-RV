package fr.gsb.rv.visiteur.modeles;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import fr.gsb.rv.visiteur.entites.Visiteur;
import org.json.JSONException;
import org.json.JSONObject;

public class serviceGsb {
/*
    String url = "http://192.168.1.29:5000/visiteur/a131/azerty"
    Response.Listener<JSONObject> ecouteurReponse = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try  {
                Visiteur visiteur = new Visiteur(null, null, null, null);
                visiteur.setNom(response.getString("vis_nom"));
            }catch(JSONException e){
                Log.e("APP-RV", "ERREUR JSON : " + e.getMessage());
            }
        }
    };

    Response.ErrorListener ecouteurError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("APP-RV", "ERREUR HTTP : " + error.getMessage());
        }
    } ;

    JsonObjectRequest requete = new JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            ecouteurReponse,
            ecouteurError
            );
    RequestQueue fileRequetes = Volley.newRequestQueue(this);
    */
}
