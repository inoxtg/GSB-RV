package fr.gsb.rv.visiteur

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import fr.gsb.rv.visiteur.adapteurs.RapportAdapter
import fr.gsb.rv.visiteur.dialogs.DeconnectionDialog
import fr.gsb.rv.visiteur.dialogs.RetourDialog
import fr.gsb.rv.visiteur.entites.Praticien
import fr.gsb.rv.visiteur.entites.RapportVisite
import fr.gsb.rv.visiteur.technique.SessionRapport
import fr.gsb.rv.visiteur.technique.SessionUser
import java.util.*

class ConsulterActivity : AppCompatActivity() {

    var rapports = mutableListOf<RapportVisite>()
    var rapportsAdapter = RapportAdapter(this@ConsulterActivity, rapports)


    val thisVisiteur = SessionUser.getLevisiteur()
    val ip: String = BuildConfig.SERVER_URL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulter)


        //NOM PRENOM SESSION

        val tvNomVisi: TextView = findViewById(R.id.nomVisi)
        val tvPrenomVisi: TextView = findViewById(R.id.prenomVisi)

        tvNomVisi.text = thisVisiteur.nom.uppercase(Locale.getDefault())
        tvPrenomVisi.text = thisVisiteur.prenom

        val lvRapports: ListView = findViewById(R.id.lvRapports)
        lvRapports.adapter = rapportsAdapter
        lvRapports.setOnItemClickListener { adapterView, vue, position, id ->
            val rapChoisis: RapportVisite = rapports.get(position)
            SessionRapport.ouvrir(rapChoisis)
            val intent = Intent(this@ConsulterActivity, RapportActivity::class.java)
            startActivity(intent)
        }
    }
    fun seDeconnecter(vue: View) {
        DeconnectionDialog().show(this.supportFragmentManager, DeconnectionDialog.TAG)
    }

    fun retour(vue: View) {
        RetourDialog().show(this.supportFragmentManager, RetourDialog.TAG)
    }
    fun validerDate(vue: View) {

        rapports.clear()
        rapportsAdapter.notifyDataSetChanged()

        val date: DatePicker = findViewById(R.id.datePicker)
        val month: Int = date.month + 1
        val year: String = date.year.toString()
        val matr: String = thisVisiteur.matricule

        val url = "$ip/rapports/$matr/$month/$year"

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                var i = 0
                while (i < response.length()){

                    var lu: String

                    val praticien = Praticien()
                    praticien.nom = response.getJSONObject(i).getString("pra_nom")
                    praticien.prenom = response.getJSONObject(i).getString("pra_prenom")
                    praticien.cp = response.getJSONObject(i).getString("pra_cp")
                    praticien.ville = response.getJSONObject(i).getString("pra_ville")

                    val rapportVisite = RapportVisite()
                    rapportVisite.leVisiteur = thisVisiteur
                    rapportVisite.lePraticien = praticien
                    rapportVisite.numero = Integer.parseInt(response.getJSONObject(i).getString("rap_num"))
                    rapportVisite.bilan = response.getJSONObject(i).getString("rap_bilan")
                    rapportVisite.dateVisite = response.getJSONObject(i).getString("rap_date_visite")
                    rapportVisite.dateRedac = response.getJSONObject(i).getString("rap_date_redaction")
                    rapportVisite.motif = response.getJSONObject(i).getString("mot_libelle")
                    rapportVisite.coefConfiance = Integer.parseInt(response.getJSONObject(i).getString("rap_coef_confiance"))
                    rapportVisite.lu = response.getJSONObject(i).getString("rap_lu") == "1"

                    rapports.add(rapportVisite)
                    i += 1
                }
                rapports.sortBy {  it.numero  }
                rapportsAdapter.notifyDataSetChanged()
            },
            {
                Log.i("Error : ", it.toString())
                Toast.makeText(this, "Aucun rapports trouvé pour le mois et l'année séléctionnés", Toast.LENGTH_LONG).show()
            })
        requestQueue.add(request)
    }
}
