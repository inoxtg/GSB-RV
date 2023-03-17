package fr.gsb.rv.visiteur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import fr.gsb.rv.visiteur.dialogs.DeconnectionDialog
import fr.gsb.rv.visiteur.dialogs.HelpDialog
import fr.gsb.rv.visiteur.technique.Session

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val tvNomVisi: TextView = findViewById(R.id.nomVisi)
        val tvPrenomVisi: TextView = findViewById(R.id.prenomVisi)

        tvNomVisi.setText(this.intent.getStringExtra("nom"))
        tvPrenomVisi.setText(this.intent.getStringExtra("prenom"))

    }
    fun consulter(vue: View){
        val intent = Intent(this@MenuActivity, ConsulterActivity::class.java)
        intent.putExtra("nom",this.intent.getStringExtra("nom"))
        intent.putExtra("prenom", this.intent.getStringExtra("prenom"))
        startActivity(intent)
    }
    fun saisir(vue: View){

    }
    fun help(vue: View){
        HelpDialog().show(this.supportFragmentManager, HelpDialog.TAG)
    }
    fun seDeconnecter(vue: View){
        DeconnectionDialog().show(this.supportFragmentManager, DeconnectionDialog.TAG)
    }
}