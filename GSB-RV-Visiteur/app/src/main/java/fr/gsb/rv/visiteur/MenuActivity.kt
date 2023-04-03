package fr.gsb.rv.visiteur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import fr.gsb.rv.visiteur.dialogs.DeconnectionDialog
import fr.gsb.rv.visiteur.dialogs.HelpDialog
import fr.gsb.rv.visiteur.entites.Visiteur
import fr.gsb.rv.visiteur.technique.SessionUser
import java.util.*

class MenuActivity : AppCompatActivity() {

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
    fun consulter(vue: View){
        val intent = Intent(this@MenuActivity, ConsulterActivity::class.java)
        startActivity(intent)
    }
    fun saisir(vue: View){
        val intent = Intent(this@MenuActivity, SaisirActivity::class.java)
        startActivity(intent)
    }
    fun help(vue: View){
        HelpDialog().show(this.supportFragmentManager, HelpDialog.TAG)
    }
    fun seDeconnecter(vue: View){
        DeconnectionDialog().show(this.supportFragmentManager, DeconnectionDialog.TAG)
    }
}