package fr.gsb.rv.visiteur

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.gsb.rv.visiteur.entites.Visiteur

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun annuler(vue: View){
        val teMatr: EditText = findViewById(R.id.teMatr)
        val teMdp: EditText = findViewById(R.id.teMdp)
        teMatr.setText("")
        teMdp.setText("")
    }

    fun seConnecter(vue: View){
        val teMatr: EditText = findViewById(R.id.teMatr)
        val teMdp: EditText = findViewById(R.id.teMdp)

        val matr: String = teMatr.getText().toString()
        val mdp: String = teMdp.getText().toString()

        val visiteur = Visiteur()
        val url = "http://192.168.1.29:5000/visiteur/$matr/$mdp"

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET, url,null,
            { response ->
                visiteur.nom = response.getString("vis_nom")
                visiteur.prenom = response.getString("vis_prenom")
                visiteur.matricule = matr
                visiteur.password = mdp

                val intent = Intent(this@MainActivity, MenuActivity::class.java)
                startActivity(intent)
            },
            {
                Toast.makeText(this,"Mot de passe ou Matricule invalide", Toast.LENGTH_LONG).show()
        })
        requestQueue.add(request)
    }
}