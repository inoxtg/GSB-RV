package fr.gsb.rv.visiteur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import fr.gsb.rv.visiteur.dialogs.DeconnectionDialog
import fr.gsb.rv.visiteur.dialogs.RetourDialog
/*
TODO :
 -Definir des textview avec :
     Numero de rapport
     Date visite
     Date redac
     Pra nom
     Pra prenom
     Pra cp
     Pra ville
  -Instancier avec les valeurs du plus récent rapport (premier dans la list vu que ASC)
  -OnClick 'valider' modifier ces valeurs avec celles retournées
  -Carrousel avec les rv du mois et année selectionnées
  ---------Pas de caroussel mais des petites icones des rapport puis onClick affichage  ?  |||||||||||||||| Me semble la meilleure idée cf carnet pour visu
  ---------Si un seul caroussel invisible ?
 */
class ConsulterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulter)

        val tvNomVisi: TextView = findViewById(R.id.nomVisi)
        val tvPrenomVisi: TextView = findViewById(R.id.prenomVisi)

        tvNomVisi.setText(this.intent.getStringExtra("nom"))
        tvPrenomVisi.setText(this.intent.getStringExtra("prenom"))
    }
    fun seDeconnecter(vue: View){
        DeconnectionDialog().show(this.supportFragmentManager, DeconnectionDialog.TAG)
    }
    fun retour(vue: View){
        RetourDialog().show(this.supportFragmentManager, RetourDialog.TAG)
    }
}