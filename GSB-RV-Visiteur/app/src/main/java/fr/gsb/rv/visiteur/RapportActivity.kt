package fr.gsb.rv.visiteur

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.gsb.rv.visiteur.adapteurs.MedicamentsAdapter
import fr.gsb.rv.visiteur.dialogs.DeconnectionDialog
import fr.gsb.rv.visiteur.dialogs.MedicamentsInformationsDialog
import fr.gsb.rv.visiteur.dialogs.RetourDialog
import fr.gsb.rv.visiteur.entites.*
import fr.gsb.rv.visiteur.technique.SessionRapport
import fr.gsb.rv.visiteur.technique.SessionUser
import java.util.*

class RapportActivity : AppCompatActivity() {

    var thisVisiteur = SessionUser.getLevisiteur()
    val ip: String = BuildConfig.SERVER_URL
    val thisRapport = SessionRapport.getLeRapport()
    var medicamentChoisis: Medicament = Medicament()

    var medicamentOffert = mutableListOf<MedicamentOffert>()
    var medicamentsAdapter = MedicamentsAdapter(this@RapportActivity, medicamentOffert)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rapport)


        val tvNomVisi: TextView = findViewById(R.id.nomVisi)
        val tvPrenomVisi: TextView = findViewById(R.id.prenomVisi)

        tvNomVisi.text = thisVisiteur.nom.uppercase(Locale.getDefault())
        tvPrenomVisi.text = thisVisiteur.prenom

        val lvMedicaments: ListView = findViewById(R.id.lvMedicaments)
        this.fillListViewWithMedicaments()
        this.fillRapportsInformations()
        lvMedicaments.adapter = this.medicamentsAdapter
        lvMedicaments.setOnItemClickListener { adapterView, vue, position, id ->

            val code: String = this.medicamentOffert[position].leMedicament.depotLegal
            val url = "$ip/medicament/$code"

            val requestQueue: RequestQueue = Volley.newRequestQueue(this)
            val request = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->

                    medicamentChoisis.depotLegal = code
                    medicamentChoisis.nom = response.getString("med_nomcommercial")
                    medicamentChoisis.code = response.getString("fam_code")
                    medicamentChoisis.composition = response.getString("med_composition")
                    medicamentChoisis.effet = response.getString("med_effets")
                    medicamentChoisis.indication = response.getString("med_contreindic")

                    Log.i("LE MEDICAMENT response", medicamentChoisis.toString())

                    val dialog = MedicamentsInformationsDialog.newInstance(medicamentChoisis)
                    dialog.show(supportFragmentManager, "MedicamentsInformationsDialog")

                },
                {
                    Log.i("Error : ", it.toString())
                })
            requestQueue.add(request)
        }
    }

    @SuppressLint("SetTextI18n")
    fun fillRapportsInformations(){
        val tvNumeroRapport: TextView = findViewById(R.id.tvRapportNumero)
        val tvDateVisiteRapport: TextView = findViewById(R.id.tvRapportDateVisite)
        val tvPraNom: TextView = findViewById(R.id.tvPraNom)
        val tvPraPrenom: TextView = findViewById(R.id.tvPraPrenom)
        val tvPraVille: TextView = findViewById(R.id.tvPraVille)
        val tvPraCp: TextView = findViewById(R.id.tvPraCp)
        val tvRapportDateRedaction: TextView = findViewById(R.id.tvRapportDateRedaction)
        val tvRapportBilan: TextView = findViewById(R.id.tvRapportBilan)
        val tvRapportMotif: TextView = findViewById(R.id.tvRapportMotif)

        tvNumeroRapport.text = thisRapport.numero.toString()
        tvDateVisiteRapport.text = "Le : " + thisRapport.dateVisite
        tvPraNom.text = thisRapport.lePraticien.nom
        tvPraPrenom.text = thisRapport.lePraticien.prenom
        tvPraVille.text = thisRapport.lePraticien.ville
        tvPraCp.text = thisRapport.lePraticien.cp
        tvRapportDateRedaction.text = thisRapport.dateRedac
        tvRapportBilan.text = thisRapport.bilan
        tvRapportMotif.text = thisRapport.motif
    }
    fun fillListViewWithMedicaments(){

        medicamentOffert.clear()

        val url = "$ip/rapports/echantillons/${thisVisiteur.matricule}/${thisRapport.numero}"

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                var i = 0
                while (i < response.length()) {

                    val medicament = MedicamentOffert()
                    medicament.leMedicament.nom = response.getJSONObject(i).getString("med_nomcommercial")
                    medicament.leMedicament.depotLegal = response.getJSONObject(i).getString("med_depotlegal")
                    medicament.quantite = Integer.parseInt(response.getJSONObject(i).getString("off_quantite"))

                    medicamentOffert.add(medicament)
                    i += 1
                }
                medicamentOffert.sortBy {  it.leMedicament.nom  }
                medicamentsAdapter.notifyDataSetChanged()
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