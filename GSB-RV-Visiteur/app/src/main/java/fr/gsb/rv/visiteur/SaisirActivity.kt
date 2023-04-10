package fr.gsb.rv.visiteur

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
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
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import fr.gsb.rv.visiteur.adapteurs.MedicamentsAdapter
import fr.gsb.rv.visiteur.adapteurs.MedicamentsCompletAdapter
import fr.gsb.rv.visiteur.adapteurs.MotifAdapter
import fr.gsb.rv.visiteur.adapteurs.PraticiensAdapter
import fr.gsb.rv.visiteur.dialogs.DeconnectionDialog
import fr.gsb.rv.visiteur.dialogs.RetourDialog
import fr.gsb.rv.visiteur.entites.*
import fr.gsb.rv.visiteur.services.PraticiensService
import fr.gsb.rv.visiteur.technique.SessionUser
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class SaisirActivity : AppCompatActivity() {


    val ip: String = BuildConfig.SERVER_URL
    private lateinit var requestQueue: RequestQueue
    lateinit var thisVisiteur: Visiteur
    val retryPolicy: DefaultRetryPolicy = DefaultRetryPolicy(
        0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    )

    var coefConfiance: Int = -1
    val listCoef = mutableListOf<Int>(0,1,2,3,4,5)

    var praticiens: MutableList<Praticien> = mutableListOf()
    var praticien: Praticien = Praticien()
    var praTest = Praticien(-1,"Praticiens : ","","","")

    var motifs: MutableList<Motif> = mutableListOf()
    var autreMotif: String = ""
    var motif: Motif = Motif()
    var motTest: Motif = Motif(-1,"Motif : ")

    lateinit var adapaterLvMedicament: MedicamentsAdapter
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

        requestQueue = Volley.newRequestQueue(this)

        Thread.sleep(100)
        praticiens = this.getLesPraticiens()
        Thread.sleep(750)
        medicaments = this.getLesMedicaments()
        Thread.sleep(750)
        motifs = this.getMotif()


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saisir)

        // PRATICIENS SPINNER

        spinnerPraticien = findViewById(R.id.spinnerPraticiens)
        val adapterSpinnerPraticien = PraticiensAdapter(this@SaisirActivity, praticiens)
        spinnerPraticien.adapter = adapterSpinnerPraticien
        spinnerPraticien.setSelection(0)
        spinnerPraticien.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(praticiens[p2] != praTest ){
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
        spinnerCoefConfiance.setSelection(0)
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
        adapaterLvMedicament = MedicamentsAdapter(this@SaisirActivity, medicamentOffertsChoisis)
        lvMedicaments.adapter = adapaterLvMedicament
        lvMedicaments.setOnItemClickListener { _, _, pos, _ ->
            confirmationSuppressionDialog(medicamentOffertsChoisis[pos], adapaterLvMedicament)
            adapaterLvMedicament.notifyDataSetChanged()
        }

        //MOTIFS SPINNER

        spinnerMotif= findViewById(R.id.spinnerMotif)
        val adapterSpinnerMotif = MotifAdapter(this@SaisirActivity, motifs)
        spinnerMotif.adapter = adapterSpinnerMotif
        spinnerMotif.setSelection(0)
        spinnerMotif.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(motifs[p2] != motTest){
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
        spinnerMedicament.setSelection(0)
        spinnerMedicament.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(medicaments[p2] != medTest ){
                    ajouterMedicamentDialog(medicaments[p2])
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
    //VERIFICATION DATA
    fun verifierAvantValider(): Boolean{
        if(autreMotif == "" && motif.numero == -1){
            this.toastWarning("Motif Invalide")
            return false
        }else if(praticien.numero == -1){
            this.toastWarning("Praticien Invalide")
            return false
        }else if(coefConfiance == -1){
            this.toastWarning("Coef Confiance Invalide")
            return false
        }else if(findViewById<TextInputEditText>(R.id.tvRapportBilan).text.toString() == ""){
            this.toastWarning("Bilan Invalide")
            return false
        }
        return true
    }
    // CLEAR DAtA

    fun clearData(){
        this.medicamentOffertsChoisis.clear()
        this.tvCpPra.text = ""
        this.tvVillePra.text = ""
        this.tvNomPra.text = ""
        this.tvPrenomPra.text = ""
        this.coefConfiance = 0
        this.spinnerMedicament.setSelection(0)
        this.spinnerPraticien.setSelection(0)
        this.spinnerMotif.setSelection(0)
        this.adapaterLvMedicament.notifyDataSetChanged()

    }

    //DIALOG
    fun seDeconnecter(vue: View) {
        DeconnectionDialog().show(this.supportFragmentManager, DeconnectionDialog.TAG)
    }
    fun retour(vue: View) {
        RetourDialog().show(this.supportFragmentManager, RetourDialog.TAG)
    }
    fun ajouterAutreMotifDialog(vue: View){
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
    fun confirmationSuppressionDialog(med: MedicamentOffert, adapter: MedicamentsAdapter){
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
    fun ajouterMedicamentDialog(med: Medicament){
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
                if(quantite != 0) {
                    val medOffert = MedicamentOffert(med, quantite)
                    for (medic: MedicamentOffert in medicamentOffertsChoisis){
                        if(medic.leMedicament.nom == medOffert.leMedicament.nom){
                            medicamentOffertsChoisis.remove(medic)
                        }
                    }
                    medicamentOffertsChoisis.add(medOffert)
                    dialog.dismiss()
                }else{
                    Toast.makeText(this@SaisirActivity,"La quantité doit être supérieur à 0 ",Toast.LENGTH_SHORT).show()
                }
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
    //GET
    fun getLesMedicaments(): MutableList<Medicament>{

        medicaments.clear()

        val url = "$ip/medicaments"
        val medicaments = mutableListOf<Medicament>()
        medicaments.add(0,medTest)

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
            },
            {
                Log.i("Error Medicaments : ", it.message.toString())
            })
        request.retryPolicy = retryPolicy
        requestQueue.add(request)

        return medicaments
    }
    fun getMotif(): MutableList<Motif> {

        motifs.clear()

        val url = "$ip/motifs"
        val motifs = mutableListOf<Motif>()
        motifs.add(0, motTest)

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
        request.retryPolicy = retryPolicy
        requestQueue.add(request)
        return motifs
    }
    fun getLesPraticiens(): MutableList<Praticien> {

        praticiens.clear()

        val url = "$ip/praticiens"
        val praticiens = mutableListOf<Praticien>()
        praticiens.add(0, praTest)

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
        request.retryPolicy = retryPolicy
        requestQueue.add(request)
        return praticiens
    }

    //POST
    fun ajouterRapport() {

        val url = "$ip/ajouter/rapports"

        val date: DatePicker = findViewById(R.id.datePickerDateVisite)
            val month: Int = date.month + 1
            val year: String = date.year.toString()
            val day: String = date.dayOfMonth.toString()
        val dateStr = "$year-$month-$day"

        val params = JSONObject()

        params.put("matricule", thisVisiteur.matricule)
        params.put("praticien", praticien.numero.toString())
        params.put("motif", motif.numero.toString())
        params.put("dateVisite", dateStr)
        params.put("dateRedaction", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE).toString())
        params.put("bilan", findViewById<TextInputEditText>(R.id.tvRapportBilan).text.toString())
        params.put("coefConfiance", coefConfiance.toString())

        val requete = JsonObjectRequest(
            Request.Method.POST, url, params,
            {
                Thread.sleep(200)
                ajouterMedicaments(Integer.parseInt(it.getString("numRapport")))
                Log.i("info rapport num : ", it.getString("numRapport"))
            },
            { error ->
                Log.e("INFO POST RAPPORT", "Erreur HTTP :" + "--" + error.message + "--")
            }
        )
        requestQueue.add(requete)
    }
    fun ajouterMedicaments(numRapport: Int){

        val matricule = thisVisiteur.matricule
        val url = "$ip/rapports/echantillons/$matricule/$numRapport"
        val params = JSONArray()
        //PARAMS
        var i = 0
        while ( i < medicamentOffertsChoisis.size){
            val echantillons = JSONObject()

            echantillons.put("med_depotlegal", medicamentOffertsChoisis[i].leMedicament.depotLegal)
            echantillons.put("off_quantite", medicamentOffertsChoisis[i].quantite)

            params.put(echantillons)
            i += 1
        }

        val requete = JsonArrayRequest(
            Request.Method.POST, url, params,
            {
                val nbOfrres = it.get(0).toString()
                Toast.makeText(this@SaisirActivity,"Rapport enregistré ! ",Toast.LENGTH_LONG).show()


            },
            { error ->
                Log.e("INFO POST MEdicaments", "Erreur HTTP :" + "--" + error.message + "--")
            }
        )
        requestQueue.add(requete)
    }
    @SuppressLint("SimpleDateFormat")
    fun valider(vue: View){
        if(this.verifierAvantValider()) {
            ajouterRapport()
            Thread.sleep(100)
            this.clearData()
        }
    }
}