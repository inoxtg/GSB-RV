package fr.gsb.rv.visiteur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import fr.gsb.rv.visiteur.dialogs.DeconnectionDialog
import fr.gsb.rv.visiteur.dialogs.RetourDialog
import fr.gsb.rv.visiteur.entites.MedicamentOffert
import fr.gsb.rv.visiteur.entites.Praticien
import fr.gsb.rv.visiteur.entites.RapportVisite
import fr.gsb.rv.visiteur.entites.Visiteur
import fr.gsb.rv.visiteur.technique.SessionRapport
import fr.gsb.rv.visiteur.technique.SessionUser
import java.io.Serializable
import java.util.*

class RapportActivity : AppCompatActivity() {

    var thisVisiteur = Visiteur()
    val ip: String = BuildConfig.SERVER_URL
    val thisRapport = SessionRapport.getLeRapport()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rapport)

        thisVisiteur = SessionUser.getLevisiteur()

        val tvNomVisi: TextView = findViewById(R.id.nomVisi)
        val tvPrenomVisi: TextView = findViewById(R.id.prenomVisi)

        tvNomVisi.text = thisVisiteur.nom.uppercase(Locale.getDefault())
        tvPrenomVisi.text = thisVisiteur.prenom

        val url = "$ip/rapports/echantillons/${thisVisiteur.matricule}/${thisRapport.numero}"
        var medicaments = mutableListOf<MedicamentOffert>()


        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                var i = 0
                while (i < response.length()) {

                    val medicament = MedicamentOffert()
                    medicament.nom = response.getJSONObject(i).getString("med_nomcommercial")
                    medicament.quantite = Integer.parseInt(response.getJSONObject(i).getString("off_quantite"))

                    medicaments.add(medicament)

                    i += 1
                }
            },
            {
                Log.i("Error : ", it.toString())
            })
        requestQueue.add(request)
    }
    fun seDeconnecter(vue: View) {
        DeconnectionDialog().show(this.supportFragmentManager, DeconnectionDialog.TAG)
    }

    fun retour(vue: View) {
        RetourDialog().show(this.supportFragmentManager, RetourDialog.TAG)
    }
}