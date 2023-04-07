package fr.gsb.rv.visiteur

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import fr.gsb.rv.visiteur.dialogs.DeconnectionDialog
import fr.gsb.rv.visiteur.dialogs.RetourDialog
import fr.gsb.rv.visiteur.entites.Medicament
import fr.gsb.rv.visiteur.entites.Visiteur
import fr.gsb.rv.visiteur.technique.SessionUser
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class SaisirActivity : AppCompatActivity() {

    var autreMotif: String = ""
    var motifs = mutableListOf<String>()
    var thisVisiteur = Visiteur()
    val ip: String = BuildConfig.SERVER_URL
    var motif: String = ""
    var medicamentsComplet = mutableListOf<Medicament>()
    var medicamentsNom = mutableListOf<String>()
    var medicamentOffertsChoisis = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {

        this.getMedicaments()
        this.getMotif()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saisir)

        thisVisiteur = SessionUser.getLevisiteur()

        val lvMedicaments: ListView = findViewById(R.id.lvMedicmentsAll)
        val adapterLvMedicament: ArrayAdapter<String> = ArrayAdapter(this,
            android.R.layout.simple_list_item_single_choice,
            medicamentOffertsChoisis)
        lvMedicaments.adapter = adapterLvMedicament

        val spinnerMotif: Spinner = findViewById(R.id.spinnerMotif)
        val adapterMotif: ArrayAdapter<String> = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item,
            motifs)
        adapterMotif.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMotif.adapter = adapterMotif
        spinnerMotif.setOnItemSelectedListener(
             object : OnItemClickListener, OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.i("INFO", "TEST 1")

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.i("INFO", "TEST 2 ")

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.i("INFO", "TEST 3")

            }

        })

        val spinnerMedicament: Spinner = findViewById(R.id.spinnerMedicaments)
        val adapterMedicament: ArrayAdapter<String> = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item,
            medicamentsNom)
        spinnerMedicament.adapter = adapterMedicament
        spinnerMedicament.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                Log.i("INFO", "TEST")
                val selectedItem = parent.getItemAtPosition(position).toString()
                Log.i("MEDICAMENT CHOISIS",selectedItem)
                medicamentOffertsChoisis.add(medicamentsNom[position])
                adapterLvMedicament.notifyDataSetChanged()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.i("INFO", "TEST NOTHI9NG")
            }
        })


        val tvNomVisi: TextView = findViewById(R.id.nomVisi)
        val tvPrenomVisi: TextView = findViewById(R.id.prenomVisi)

        tvNomVisi.setText(thisVisiteur.nom.uppercase(Locale.getDefault()))
        tvPrenomVisi.setText(thisVisiteur.prenom)

        val tvDateRedac: TextView = findViewById(R.id.tiRapportDateRedaction)
        val dateJour: String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        tvDateRedac.setText(dateJour)
    }
    fun seDeconnecter(vue: View) {
        DeconnectionDialog().show(this.supportFragmentManager, DeconnectionDialog.TAG)
    }

    fun retour(vue: View) {
        RetourDialog().show(this.supportFragmentManager, RetourDialog.TAG)
    }
    fun ajouterMotif(vue: View){
        val text = EditText(this)
        text.hint = "Motif..."
        val tvMotif: TextView = findViewById(R.id.tvAutreMotif)
        val spinnerMotif: Spinner = findViewById(R.id.spinnerMotif)


        AlertDialog.Builder(this)
            .setTitle("Ajouter Motif")
            .setView(text)
            .setPositiveButton("Ok") { dialog, _ ->

                autreMotif = text.text.toString()
                spinnerMotif.isVisible = false

                tvMotif.isVisible = true
                tvMotif.setText(autreMotif)

                Log.i("info dans modal ", autreMotif)
                dialog.dismiss()
            }
            .setNegativeButton("Annuler") { dialog, _ ->
                tvMotif.setText("")
                tvMotif.isVisible = false
                spinnerMotif.isVisible = true
            }
            .show()

        Log.i("info", autreMotif)
    }

    fun getMedicaments(){

        val url = "$ip/medicaments"

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                var i = 0
                while (i < response.length()){
                    val medicament = Medicament()
                    medicament.code =  response.getJSONObject(i).getString("fam_code")
                    medicament.nom =  response.getJSONObject(i).getString("med_nomcommercial")
                    medicament.effet = response.getJSONObject(i).getString("med_effets")
                    medicament.composition =  response.getJSONObject(i).getString("med_composition")
                    medicament.depotLegal = response.getJSONObject(i).getString("med_depotlegal")
                    medicament.indication = response.getJSONObject(i).getString("med_contreindic")

                    medicamentsComplet.add(medicament)
                    medicamentsNom.add(medicament.nom + "-" + medicament.code)

                    i += 1
                }
                medicamentsComplet.sortBy {  it.nom  }
            },
            {
                Log.i("Error Medicaments : ", it.toString())
            })
        requestQueue.add(request)

    }

    fun getMotif(){
        val url = "$ip/motifs"

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                var i = 0
                while (i < response.length()){
                        motifs.add(response.getJSONObject(i).getString("mot_libelle"))
                    i += 1
                }
            },
            {
                Log.i("Error Motif : ", it.toString())
            })
        requestQueue.add(request)
    }

}