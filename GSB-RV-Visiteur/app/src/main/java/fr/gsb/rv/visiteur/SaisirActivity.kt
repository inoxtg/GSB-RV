package fr.gsb.rv.visiteur

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import fr.gsb.rv.visiteur.dialogs.DeconnectionDialog
import fr.gsb.rv.visiteur.dialogs.RetourDialog
import fr.gsb.rv.visiteur.entites.Visiteur
import fr.gsb.rv.visiteur.technique.SessionUser
import java.util.*

class SaisirActivity : AppCompatActivity() {

    var autreMotif: String = ""
    var thisVisiteur = Visiteur()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        thisVisiteur = SessionUser.getLevisiteur()

        val tvNomVisi: TextView = findViewById(R.id.nomVisi)
        val tvPrenomVisi: TextView = findViewById(R.id.prenomVisi)

        tvNomVisi.setText(thisVisiteur.nom.uppercase(Locale.getDefault()))
        tvPrenomVisi.setText(thisVisiteur.prenom)
    }
    fun seDeconnecter(vue: View) {
        DeconnectionDialog().show(this.supportFragmentManager, DeconnectionDialog.TAG)
    }

    fun retour(vue: View) {
        RetourDialog().show(this.supportFragmentManager, RetourDialog.TAG)
    }
    fun ajouterMotif(vue: View){
        val txtUrl = EditText(this)
        txtUrl.hint = "Motif..."

        AlertDialog.Builder(this)
            .setTitle("Ajouter Motif")
            .setView(txtUrl)
            .setPositiveButton("Ok") { dialog, whichButton ->
                autreMotif = txtUrl.text.toString()
                Log.i("info dans modal ", autreMotif)
                dialog.dismiss()
            }
            .setNegativeButton("Annuler") { dialog, whichButton -> }
            .show()

        Log.i("info", autreMotif)
    }
}