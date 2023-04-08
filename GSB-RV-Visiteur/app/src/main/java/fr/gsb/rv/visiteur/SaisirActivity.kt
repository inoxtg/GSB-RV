package fr.gsb.rv.visiteur

import android.graphics.Color
import android.graphics.ColorFilter
import android.os.Bundle
import android.text.Html
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import fr.gsb.rv.visiteur.adapteurs.MedicamentsAdapter
import fr.gsb.rv.visiteur.adapteurs.MedicamentsCompletAdapter
import fr.gsb.rv.visiteur.adapteurs.MotifAdapter
import fr.gsb.rv.visiteur.adapteurs.PraticiensAdapter
import fr.gsb.rv.visiteur.dialogs.DeconnectionDialog
import fr.gsb.rv.visiteur.dialogs.RetourDialog
import fr.gsb.rv.visiteur.entites.*
import fr.gsb.rv.visiteur.technique.SessionUser
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class SaisirActivity : AppCompatActivity() {

    lateinit var thisVisiteur: Visiteur
    val ip: String = BuildConfig.SERVER_URL

    var coefConfiance: Int = -1
    val listCoef = mutableListOf<Int>(0,1,2,3,4,5)

    var praticiens: MutableList<Praticien> = mutableListOf()
    var praticien: Praticien = Praticien()
    var praTest = Praticien(-1,"Praticiens : ","","","")

    var motifs: MutableList<Motif> = mutableListOf()
    var autreMotif: String = ""
    var motif: Motif = Motif()
    var motTest: Motif = Motif(-1,"Motif : ")

    var medicaments: MutableList<Medicament> = mutableListOf()
    var medicamentOffertsChoisis = mutableListOf<MedicamentOffert>()
    var medTest = Medicament(" ","Médicaments : "," "," "," "," ")

    lateinit var spinnerMedicament: Spinner
    lateinit var spinnerMotif: Spinner
    lateinit var lvMedicaments: ListView
    lateinit var spinnerCoefConfiance: Spinner
    lateinit var spinnerPraticien: Spinner
    lateinit var tvNomPra: TextView
    lateinit var tvPrenomPra: TextView
    lateinit var tvVillePra: TextView
    lateinit var tvCpPra: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        praticiens = this.getLesPraticiens()
        medicaments = this.getLesMedicaments()
        motifs = this.getMotif()


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saisir)

        // PRATICIENS SPINNER

        spinnerPraticien = findViewById(R.id.spinnerPraticiens)
        val adapterSpinnerPraticien = PraticiensAdapter(this@SaisirActivity, praticiens)
        spinnerPraticien.adapter = adapterSpinnerPraticien
        spinnerPraticien.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p2 != 0 || praticiens[p2] != praTest ){
                    praticien = praticiens[p2]
                    Log.i("info clicked", praticien.toString())
                    tvNomPra = findViewById(R.id.tiPraNom)
                    tvNomPra.text = praticien.nom

                    tvPrenomPra = findViewById(R.id.tiPraPrenom)
                    tvPrenomPra.text = praticien.prenom

                    tvVillePra = findViewById(R.id.tiPraVille)
                    tvVillePra.text = praticien.ville

                    tvCpPra = findViewById(R.id.tvPraCp)
                    tvCpPra.text = praticien.cp
                }else{
                    Toast.makeText(this@SaisirActivity,"Choissisez un praticien...",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        // COEF SPINNER

        spinnerCoefConfiance = findViewById(R.id.spinnerCoefConfiance)
        spinnerCoefConfiance.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listCoef
        )
        spinnerCoefConfiance.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.i("INFO CLIKED ", p2.toString())
                coefConfiance = p2
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        //LIST MEDICAMENTS AJOUTES

        lvMedicaments = findViewById(R.id.lvMedicmentsAll)
        val adapaterLvMedicament = MedicamentsAdapter(this@SaisirActivity, medicamentOffertsChoisis)
        lvMedicaments.adapter = adapaterLvMedicament
        lvMedicaments.setOnItemClickListener { _, _, pos, _ ->
            confirmationSuppression(medicamentOffertsChoisis[pos], adapaterLvMedicament)
            adapaterLvMedicament.notifyDataSetChanged()
        }

        //MOTIFS SPINNER

        spinnerMotif= findViewById(R.id.spinnerMotif)
        val adapterSpinnerMotif = MotifAdapter(this@SaisirActivity, motifs)
        spinnerMotif.adapter = adapterSpinnerMotif
        spinnerMotif.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p2 != 0 || motifs[p2] != motTest){
                    motif = motifs[p2]
                }else{
                    Toast.makeText(this@SaisirActivity,"Choissisez un motif...",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        //MEDICAMENTS SPINNER

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

    fun valider(vue: View){
        if(autreMotif == "" && motif.numero == -1){
            this.toastWarning("Motif Invalide")
        }else if(praticien.numero == -1){
            this.toastWarning("Praticien Invalide")
        }else if(coefConfiance == -1){
            this.toastWarning("Coef Confiance Invalide")
        }else if(findViewById<TextInputEditText>(R.id.tvRapportBilan).text.toString() == ""){
            this.toastWarning("Bilan Invalide")
        }
    }
    //DIALOG
    fun seDeconnecter(vue: View) {
        DeconnectionDialog().show(this.supportFragmentManager, DeconnectionDialog.TAG)
    }
    fun retour(vue: View) {
        RetourDialog().show(this.supportFragmentManager, RetourDialog.TAG)
    }
    fun ajouterAutreMotif(vue: View){
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
                var quantite = 0
                if(qte.text.toString() != ""){
                    quantite += Integer.parseInt(qte.text.toString())
                }
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
    fun toastWarning(info: String){
        val toast =
            Toast.makeText(this@SaisirActivity, Html.fromHtml("<font color='#f94d59' ><b>$info</b></font>"), Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.view?.setBackgroundColor(Color.GRAY)
        toast.show()
    }

    //SERVICE BDD
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
    fun getMotif(): MutableList<Motif> {

        motifs.clear()

        val url = "$ip/motifs"
        val motifs = mutableListOf<Motif>()
        motifs.add(motTest)

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                var i = 0
                while (i < response.length()){
                    val motif = Motif(
                        Integer.parseInt(response.getJSONObject(i).getString("mot_num")),
                        response.getJSONObject(i).getString("mot_libelle"))
                    motifs.add(motif)
                    i += 1
                }
            },
            {
                Log.i("Error Motif : ", it.toString())
            })
        requestQueue.add(request)
        return motifs
    }
    fun getLesPraticiens(): MutableList<Praticien> {
        praticiens.clear()

        val url = "$ip/praticiens"
        val praticiens = mutableListOf<Praticien>()
        praticiens.add(praTest)

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
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
                Log.i("Error Motif : ", it.toString())
            })
        requestQueue.add(request)
        return praticiens
    }
}