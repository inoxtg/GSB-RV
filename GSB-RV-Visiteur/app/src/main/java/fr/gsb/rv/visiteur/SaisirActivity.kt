package fr.gsb.rv.visiteur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import fr.gsb.rv.visiteur.dialogs.DeconnectionDialog
import fr.gsb.rv.visiteur.dialogs.RetourDialog

class SaisirActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saisir)
    }
    fun seDeconnecter(vue: View) {
        DeconnectionDialog().show(this.supportFragmentManager, DeconnectionDialog.TAG)
    }

    fun retour(vue: View) {
        RetourDialog().show(this.supportFragmentManager, RetourDialog.TAG)
    }
}