package fr.gsb.rv.visiteur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import fr.gsb.rv.visiteur.dialogs.DeconnectionDialog
import fr.gsb.rv.visiteur.dialogs.HelpDialog

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }
    fun consulter(vue: View){

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