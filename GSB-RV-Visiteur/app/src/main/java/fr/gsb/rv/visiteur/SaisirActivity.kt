package fr.gsb.rv.visiteur

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import fr.gsb.rv.visiteur.adapteurs.MedicamentsAdapter
import fr.gsb.rv.visiteur.adapteurs.MedicamentsCompletAdapter
import fr.gsb.rv.visiteur.dialogs.DeconnectionDialog
import fr.gsb.rv.visiteur.dialogs.RetourDialog
import fr.gsb.rv.visiteur.entites.Medicament
import fr.gsb.rv.visiteur.entites.MedicamentOffert
import fr.gsb.rv.visiteur.entites.Visiteur
import fr.gsb.rv.visiteur.technique.SessionUser
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class SaisirActivity : AppCompatActivity() {

    lateinit var thisVisiteur: Visiteur
    val ip: String = BuildConfig.SERVER_URL



    var motifs: MutableList<String> = mutableListOf()
    var autreMotif: String = ""
    var motif: String = ""

    var medicaments: MutableList<Medicament> = mutableListOf()
    var medicamentOffertsChoisis = mutableListOf<MedicamentOffert>()
    var medTest = Medicament(" ","Médicaments : "," "," "," "," ")

    lateinit var spinnerMedicament: Spinner
    lateinit var spinnerMotif: Spinner
    lateinit var lvMedicaments: ListView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saisir)


        lvMedicaments = findViewById(R.id.lvMedicmentsAll)
        val adapaterLvMedicament = MedicamentsAdapter(this@SaisirActivity, medicamentOffertsChoisis)
        lvMedicaments.adapter = adapaterLvMedicament
        lvMedicaments.setOnItemClickListener { _, _, pos, _ ->
            confirmationSuppression(medicamentOffertsChoisis[pos], adapaterLvMedicament)
            adapaterLvMedicament.notifyDataSetChanged()
        }

        motifs = this.getMotif()
        spinnerMotif= findViewById(R.id.spinnerMotif)
        spinnerMotif.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_single_choice,
            motifs)
        spinnerMotif.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p2 != 0){
                    motif = motifs[p2]
                }else{
                    Toast.makeText(this@SaisirActivity,"Choissisez un motif...",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


       medicaments = this.getLesMedicaments()
       spinnerMedicament = findViewById(R.id.spinnerMedicaments)
        val adapterSpinnerMedicament = MedicamentsCompletAdapter(this@SaisirActivity, medicaments)
       spinnerMedicament.adapter = adapterSpinnerMedicament

        spinnerMedicament.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p2 != 0 || medicaments[p2] != medTest ){
                    ajouterMedicament(medicaments[p2])
                    adapaterLvMedicament.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@SaisirActivity,"Choissisez un médicament...",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.i("info", "TEST !!SELECTED ")
            }
        }


        // VISITEUR

        thisVisiteur = SessionUser.getLevisiteur()
        val tvNomVisi: TextView = findViewById(R.id.nomVisi)
        val tvPrenomVisi: TextView = findViewById(R.id.prenomVisi)

        tvNomVisi.setText(thisVisiteur.nom.uppercase(Locale.getDefault()))
        tvPrenomVisi.setText(thisVisiteur.prenom)

        //DATE REDACTION

        val tvDateRedac: TextView = findViewById(R.id.tiRapportDateRedaction)
        tvDateRedac.text = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
    fun seDeconnecter(vue: View) {
        DeconnectionDialog().show(this.supportFragmentManager, DeconnectionDialog.TAG)
    }

    fun retour(vue: View) {
        RetourDialog().show(this.supportFragmentManager, RetourDialog.TAG)
    }
    fun confirmationSuppression(med: MedicamentOffert, adapter: MedicamentsAdapter){
        val medNom = med.leMedicament.nom
        val medQe = med.quantite

        AlertDialog.Builder(this)

            .setTitle("Voulez_vraiment supprimer les $medQe unités de $medNom ?")
            .setPositiveButton("Ok") { dialog, _ ->
                medicamentOffertsChoisis.remove(med)
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            .setNegativeButton("Annuler") { _, _ ->
            }
            .show()
    }
    fun ajouterMedicament(med: Medicament){
        val qte = EditText(this)
        qte.inputType = InputType.TYPE_CLASS_NUMBER
        val medNom = med.nom

        AlertDialog.Builder(this)

            .setTitle("Selectionner la quantité pour le médicament : $medNom")
            .setView(qte)
            .setPositiveButton("Ok") { dialog, _ ->
                val quantite: Int = Integer.parseInt(qte.text.toString())
                medicamentOffertsChoisis.add(
                    MedicamentOffert(
                        med,
                        quantite
                    ))
                dialog.dismiss()
            }
            .setNegativeButton("Annuler") { _, _ ->
            }
            .show()
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
                tvMotif.text = autreMotif
                dialog.dismiss()
            }
            .setNegativeButton("Annuler") { _, _ ->
                tvMotif.text = ""
                tvMotif.isVisible = false
                spinnerMotif.isVisible = true
            }
            .show()
    }


    fun getLesMedicaments(): MutableList<Medicament>{

        medicaments.clear()

        val url = "$ip/medicaments"
        val medicaments = mutableListOf<Medicament>()
        medicaments.add(medTest)

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

                    medicaments.add(medicament)
                    i += 1
                }
                medicaments.sortBy {  it.nom  }
            },
            {
                Log.i("Error Medicaments : ", it.toString())
            })
        requestQueue.add(request)
        Log.i("info", medicaments.size.toString())
        return medicaments
    }

    fun getMotif(): MutableList<String> {

        motifs.clear()

        val url = "$ip/motifs"
        val motifs = mutableListOf<String>()
        motifs.add("Motifs : ")

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
        return motifs
    }
}