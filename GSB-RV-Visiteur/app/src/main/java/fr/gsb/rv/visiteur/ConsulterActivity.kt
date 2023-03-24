package fr.gsb.rv.visiteur

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.gsb.rv.visiteur.dialogs.DeconnectionDialog
import fr.gsb.rv.visiteur.dialogs.RetourDialog
import fr.gsb.rv.visiteur.entites.Praticien
import fr.gsb.rv.visiteur.entites.RapportVisite
import fr.gsb.rv.visiteur.technique.Session

class ConsulterActivity : AppCompatActivity() {

    val thisVisiteur = Session.getLevisiteur()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulter)

        val tvNomVisi: TextView = findViewById(R.id.nomVisi)
        val tvPrenomVisi: TextView = findViewById(R.id.prenomVisi)
        /**
         * TODO : Changer cette ternaire, erreur visible + problèmatique
         */
        tvNomVisi.setText(thisVisiteur?.nom?.toUpperCase() ?: "errSession")
        tvPrenomVisi.setText(thisVisiteur?.prenom ?: "errSession")
    }
    fun seDeconnecter(vue: View) {
        DeconnectionDialog().show(this.supportFragmentManager, DeconnectionDialog.TAG)
    }

    fun retour(vue: View) {
        RetourDialog().show(this.supportFragmentManager, RetourDialog.TAG)
    }
    fun validerDate(vue: View) {
        val date: DatePicker = findViewById(R.id.datePicker)
        val month: Int = date.month + 1
        val year: String = date.year.toString()
        val matr: String? = thisVisiteur?.matricule

        val ip = "http://192.168.43.59:5000"
        val url = "$ip/rapports/$matr/$month/$year"

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.i("Response length should be 12", response.length().toString())
                var i = 0
                while (i <= response.length()){

                    val praticien = Praticien()
                    praticien.nom = response.getJSONObject(i).getString("pra_nom")
                    praticien.prenom = response.getJSONObject(i).getString("pra_prenom")
                    praticien.cp = response.getJSONObject(i).getString("pra_cp")
                    praticien.ville = response.getJSONObject(i).getString("pra_ville")

                    val rapportVisite = RapportVisite()
                    rapportVisite.leVisiteur = thisVisiteur!!
                    rapportVisite.lePraticien = praticien
                    rapportVisite.numero = Integer.parseInt(response.getJSONObject(i).getString("rap_num"))
                    rapportVisite.bilan = response.getJSONObject(i).getString("rap_bilan")
                    rapportVisite.dateVisite = response.getJSONObject(i).getString("rap_date_visite")

                    Log.i("COMPTEUR", i.toString())
                    Log.i("RV : ", rapportVisite.toString())

                    i += 1
                }
            },
            {
                Log.i("Error : ", it.toString())
                Toast.makeText(this, "Aucun rapports à cette date", Toast.LENGTH_LONG).show()
            })
        requestQueue.add(request)
    }
}
